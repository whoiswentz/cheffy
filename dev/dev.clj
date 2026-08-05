(ns dev
  (:require [cheffy.server :as server]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.stuartsierra.component.repl :as cr]
            [datomic.client.api :as d]))

(defn system [_]
  (-> (io/resource "development.edn")
      (slurp)
      (edn/read-string)
      (server/create-system)))

(cr/set-init system)

(defn start-dev []
  (cr/start))

(defn stop-dev []
  (cr/stop))

(defn restart-dev []
  (cr/reset))

(comment
  (d/q '[:find ?e ?id
         :where [?e :account/account-id ?id]]
       (d/db (-> cr/system :database :conn))))