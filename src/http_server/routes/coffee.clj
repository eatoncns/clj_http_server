(ns http-server.routes.coffee
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.constants.methods :refer [GET]]
            [http-server.constants.content-type :refer [content-type]]))

(defrecord Coffee [request]
  route/Route
  (is-applicable? [this directory-served]
    (and (= (get-in this [:request :uri]) "/coffee")
         (= (get-in this [:request :method]) GET)))

  (process [this directory-served]
    (map->Response {:status 418 :headers (content-type "txt") :body "I'm a teapot"}))
)
