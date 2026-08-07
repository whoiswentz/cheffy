(ns cheffy.routes
  (:require [cheffy.recipes :as recipes]
            [io.pedestal.http.route.definition.terse :as terse]))

(defn upsert-recipe
  [request]
  {:status 200
   :body   "upsert recipes"})

(def routes
  (terse/terse-routes
    [[["/recipes" {:get  [:list-recipes `recipes/list-recipes]
                   :post [:create-recipe `upsert-recipe]}
       ["/:recipe-id" {:put [:update-recipe `upsert-recipe]}]]]]))
