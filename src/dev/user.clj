(ns user
  (:require [io.pedestal.http :as http]))

(http/start
  (http/create-server {
                       ::http/routes #{}
                       ::http/type :jetty
                       ::http/port 3000
                       }))