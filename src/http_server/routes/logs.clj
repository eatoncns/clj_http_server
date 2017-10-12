(ns http-server.routes.logs
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.logger :as logger])
  (:import [http_server.response Response]))

(defrecord Logs [request]
  route/Route
  (is-applicable? [this directory-served]
    (and (= (get-in this [:request :uri]) "/logs")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (map->Response {:status 200 :headers {} :body (slurp logger/log-file-name)}))
)
