(ns cheffy.interceptors
  (:require [datomic.client.api :as d]
            [io.pedestal.interceptor :refer [interceptor]]))

(defn inject-database
  [database]
  (interceptor
    {:name  ::inject-database
     :enter #(assoc-in % [:request :system/database] database)}))

(def db-interceptor
  (interceptor
    {:name ::db-interceptor
     :enter (fn [ctx]
              (if-let [conn (get-in ctx [:request :system/database :conn])]
                (assoc-in ctx [:request :db] (d/db conn))
                ctx))}))