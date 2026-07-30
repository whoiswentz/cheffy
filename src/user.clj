(ns user
  (:require [io.pedestal.connector :as conn]
            [io.pedestal.http.jetty :as jetty]
            [io.pedestal.http.route.definition.terse :as terse]))

(defonce system-ref (atom nil))

(defn list-recipes
  [request]
  {:status 200
   :body   "list recipes"})

(defn upsert-recipe
  [request]
  {:status 200
   :body   "upsert recipes"})

(def routes
  (terse/terse-routes
    [[:cheffy :http "localhost:8000"
      {:router :sawtooth}
      ["/recipes" {:get  [:list-recipes `list-recipes]
                   :post [:create-recipe `upsert-recipe]}
       [":recipe-id" {:put [:update-recipe `upsert-recipe]}]]]]))

(defn start-dev
  []
  (reset! system-ref
          (-> (conn/default-connector-map 3000)
              (conn/with-routes #{})
              (conn/with-default-interceptors
                :allowed-origins ["http://localhost:8080"]
                :mime-types      {"json" "application/json"
                                  "edn"  "application/edn"})
              (conn/with-routes routes)
              (jetty/create-connector nil)
              (conn/start!)))
  :started)

(defn stop-dev
  []
  (when @system-ref
    (conn/stop! @system-ref)
    :stopped))
