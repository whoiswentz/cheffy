(ns cheffy.components.api-server
  (:require [cheffy.routes :as routes]
            [com.stuartsierra.component :as component]
            [io.pedestal.connector :as conn]
            [io.pedestal.http.jetty :as jetty]
            [io.pedestal.interceptor :as interceptor]))

(defn inject-system
  [system]
  (interceptor/interceptor
    {:name ::inject-system
     :enter (fn [ctx]
              (update-in ctx [:request] merge system))}))

(defrecord ApiServer [service-map service database]
  component/Lifecycle
  (start [component]
    (let [port            (get service-map :port 3000)
          allowed-origins (get service-map :allowed-origins ["http://localhost:8080"])
          started-service (-> (conn/default-connector-map port)
                              (conn/with-default-interceptors
                                :allowed-origins allowed-origins
                                :extra-mime-types {"json" "application/json"
                                                   "edn"  "application/edn"})
                              (conn/with-interceptor (inject-system {:database database}))
                              (conn/with-routes routes/routes)
                              (jetty/create-connector nil)
                              (conn/start!))]
      (assoc component :service started-service)))

  (stop [component]
    (when service
      (conn/stop! service))
    (assoc component :service nil)))

(defn service
  [service-map]
  (map->ApiServer {:service-map service-map}))
