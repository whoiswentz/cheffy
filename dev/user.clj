(ns user
  (:require [com.stuartsierra.component.repl :as cr]
            [datomic.client.api :as d]
            [dev]))

(defn start []
  (dev/start-dev))

(defn stop []
  (dev/stop-dev))

(defn restart []
  (dev/restart-dev))
