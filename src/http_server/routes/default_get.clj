(ns http-server.routes.default-get
  (:require [http-server.response :refer [map->Response]]
            [http-server.routes.route :as route]
            [http-server.constants.methods :refer [GET HEAD]]
            [http-server.utils.functional :refer [in?]]))

(defrecord DefaultGET [request]
  route/Route
  (is-applicable? [this directory-served]
    (in? (get request :method) [GET HEAD]))

  (process [this directory-served]
    (map->Response {:status 404 :headers {} :body nil}))
)
