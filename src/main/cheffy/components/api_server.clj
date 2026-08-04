(ns cheffy.components.api-server
  (:require [cheffy.interceptors :as interceptors]
            [cheffy.routes :as routes]
            [com.stuartsierra.component :as component]
            [io.pedestal.connector :as conn]
            [io.pedestal.connector.dev :as conn-dev]
            [io.pedestal.environment :refer [dev-mode?]]
            [io.pedestal.http.jetty :as jetty]))

(defn build-connector-map
  [{:keys [host port allowed-origins]} sys-interceptors]
  (-> (conn/default-connector-map host port)
      (conn/with-interceptors (when dev-mode? conn-dev/dev-interceptors))
      (conn/with-default-interceptors
        :allowed-origins allowed-origins
        :extra-mime-types {"json" "application/json"
                           "edn"  "application/edn"})
      (conn/with-interceptors sys-interceptors)
      (conn/with-routes routes/routes)))

(defrecord ApiServer [service-map service connector-map database]
  component/Lifecycle
  (start [component]
    (let [conn-map (build-connector-map service-map [(interceptors/inject-database database)])]
      (assoc component
             :connector-map conn-map
             :service (-> conn-map
                          (jetty/create-connector nil)
                          (conn/start!)))))

  (stop [component]
    (when service
      (conn/stop! service))
    (assoc component :service nil :connector-map nil)))

(defn service
  [service-map]
  (map->ApiServer {:service-map service-map}))
