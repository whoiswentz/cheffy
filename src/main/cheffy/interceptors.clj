(ns cheffy.interceptors)

(defn inject-database
  [database]
  {:name  ::inject-database
   :enter #(assoc-in % [:request :database] database)})
