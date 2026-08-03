(ns user
  (:require [com.stuartsierra.component :as component]
            [cheffy.server :as server]))

(defonce system-ref (atom nil))

(defn start-dev
  []
  (let [system (server/create-system {:api-server {:port 3000
                                                   :allowed-origins ["http://localhost:8080"]}})]
    (reset! system-ref (component/start system))
    :started))

(defn stop-dev
  []
  (when @system-ref
    (component/stop @system-ref)
    (reset! system-ref nil)
    :stopped))

(defn restart
  []
  (stop-dev)
  (start-dev))
