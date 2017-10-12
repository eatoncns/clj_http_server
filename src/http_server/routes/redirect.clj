(ns http-server.routes.redirect
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]])
  (:import [http_server.response Response]))

(defrecord Redirect [request]
  route/Route
  (is-applicable? [this directory-served]
    (and (= (get-in this [:request :uri]) "/redirect")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (map->Response {:status 302
                    :headers {"Location" "/"}
                    :body nil}))
)
