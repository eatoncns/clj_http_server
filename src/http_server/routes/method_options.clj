(ns http-server.routes.method-options
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]])
  (:import [http_server.response Response]))

(defrecord MethodOptions [request]
  route/Route
  (is-applicable? [this directory-served]
    (= (get-in this [:request :uri]) "/method_options"))

  (process [this directory-served]
    (map->Response {:status 200
                    :headers {"Allow" "GET,HEAD,POST,OPTIONS,PUT"}
                    :body nil}))
)
