(ns http-server.routes.tea
  (:require [http-server.routes.route :as route]
            [http-server.response])
  (:import [http_server.response Response]))

(defrecord Tea [request]
  route/Route
  (is-applicable [this]
    (and (= (get-in this [:request :uri]) "/tea")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (Response. 200 {} nil))
)
