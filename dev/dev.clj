(ns dev
  (:require [cheffy.server :as server]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.stuartsierra.component.repl :as cr]
            [io.pedestal.test :as pt]
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
  (pt/response-for
    (-> cr/system :api-server :service ::http/service-fn)
    :get "/recipes"
    :headers {"Authorization" "auth|5fbf7db6271d5e0076903601"})

  (d/q '[:find ?e ?id
         :where [?e :account/account-id ?id]]
       (d/db (-> cr/system :database :conn))))