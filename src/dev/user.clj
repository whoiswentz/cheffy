(ns user
  (:require [io.pedestal.connector :as conn]
            [io.pedestal.http.jetty :as jetty]
            [cheffy.routes :as routes]))

(defonce system-ref (atom nil))

(defn start-dev
  []
  (reset! system-ref
          (-> (conn/default-connector-map 3000)
              (conn/with-routes #{})
              (conn/with-default-interceptors
                :allowed-origins ["http://localhost:8080"]
                :mime-types      {"json" "application/json"
                                  "edn"  "application/edn"})
              (conn/with-routes routes/routes)
              (jetty/create-connector nil)
              (conn/start!)))
  (println @system-ref)
  :started)

(defn stop-dev
  []
  (when @system-ref
    (conn/stop! @system-ref)
    :stopped))
