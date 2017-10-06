(ns http-server.routes.default
  (:require [http-server.response :refer [map->Response]]
            [http-server.routes.route :as route])
  (:import [http_server.response Response]))

(defrecord Default [request]
  route/Route
  (is-applicable [this]
    true)

  (process [this _]
    (map->Response {:status 200 :headers {} :body "Hello World"})))
