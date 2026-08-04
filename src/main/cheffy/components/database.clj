(ns cheffy.components.database
  (:require [com.stuartsierra.component :as component]
            [datomic.client.api :as d]))

(defrecord Database [config conn]
  component/Lifecycle

  (start [component]
    (let [db-name (select-keys config [:db-name])
          client (d/client (select-keys config [:server-type :storage-dir :system]))
          _ (d/create-database client db-name)
          conn (d/connect client db-name)]
      (assoc component :conn conn)))

  (stop [component]
    (when (= (:server-type config) :datomic-local)
      (when-let [release-db (requiring-resolve 'datomic.local/release-db)]
        (release-db (select-keys config [:system :db-name :mem]))))
    (assoc component :conn nil)))

(defn service
  [config]
  (map->Database {:config config}))
