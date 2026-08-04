(ns user
  (:require [dev]))

(defn start []
  (dev/start-dev))

(defn stop []
  (dev/stop-dev))

(defn restart []
  (dev/restart-dev))
