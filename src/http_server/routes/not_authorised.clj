(ns http-server.routes.not-authorised
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]])
  (:import [http_server.response Response]))

(defrecord NotAuthorised [request]
  route/Route
  (is-applicable? [this directory-served]
    (not (get-in this [:request :authorised])))

  (process [this directory-served]
    (map->Response {:status 401 :headers {"WWW-Authenticate" "Basic realm=authentication"} :body nil}))
)

