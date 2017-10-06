(ns http-server.routes.coffee
  (:require [http-server.routes.route :as route]
            [http-server.response])
  (:import [http_server.response Response]))

(defrecord Coffee [request]
  route/Route
  (is-applicable [this]
    (and (= (get-in this [:request :uri]) "/coffee")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (Response. 418 "I'm a teapot"))
)
