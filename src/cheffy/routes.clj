(ns cheffy.routes
  (:require [io.pedestal.http.route.definition.terse :as terse]))

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
