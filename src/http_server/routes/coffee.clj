(ns http-server.routes.coffee
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]])
  (:import [http_server.response Response]))

(defrecord Coffee [request]
  route/Route
  (is-applicable [this]
    (and (= (get-in this [:request :uri]) "/coffee")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (map->Response {:status 418 :headers {} :body "I'm a teapot"}))
)
