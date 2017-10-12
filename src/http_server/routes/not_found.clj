(ns http-server.routes.not-found
  (:require [http-server.response :refer [map->Response]]
            [http-server.routes.route :as route]
            [http-server.constants.methods :refer [GET HEAD]]
            [http-server.utils.functional :refer [in?]]))

(defrecord NotFound [request]
  route/Route
  (is-applicable? [this directory-served]
    (in? (get request :method) [GET HEAD]))

  (process [this directory-served]
    (map->Response {:status 404 :headers {} :body nil}))
)
