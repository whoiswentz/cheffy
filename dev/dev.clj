(ns dev
  (:require [cheffy.server :as server]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.stuartsierra.component.repl :as cr]))

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
