(ns http-server.routes.default
  (:require [http-server.response]
            [http-server.routes.route :as route])
  (:import [http_server.response Response]))

(defrecord Default [request]
  route/Route
  (is-applicable [this]
    true)

  (process [this _]
    (Response. 200 {} (.getBytes "Hello World"))))
