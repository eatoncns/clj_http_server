(ns http-server.routes.tea
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]])
  (:import [http_server.response Response]))

(defrecord Tea [request]
  route/Route
  (is-applicable? [this directory-served]
    (and (= (get-in this [:request :uri]) "/tea")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (map->Response {:status 200 :headers {} :body nil}))
)
