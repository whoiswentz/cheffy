(ns user
  (:require [io.pedestal.http :as http]))

(defonce system-ref (atom nil))
(defonce service-map {
                  ::http/routes #{}
                  ::http/type :jetty
                  ::http/join? false
                  ::http/port 3000
                  })

(defn start-dev []
  (reset! system-ref
          (-> service-map
              (http/create-server)
              (http/start)))
  :started)

(defn stop-dev []
  (http/stop @system-ref)
  :stopped)

(defn restart-dev []
  (stop-dev)
  (start-dev)
  :restarted)