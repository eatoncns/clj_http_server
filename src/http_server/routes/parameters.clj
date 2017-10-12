(ns http-server.routes.parameters
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]])
  (:import [http_server.response Response]))

(defn- add-param [param-string k v]
  (str param-string k " = " v "\r\n"))

(defn- format-params [params]
  (reduce-kv add-param "" params))

(defrecord Parameters [request]
  route/Route
  (is-applicable? [this directory-served]
    (and (= (get-in this [:request :uri]) "/parameters")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (map->Response {:status 200
                    :headers {}
                    :body (format-params (get-in this [:request :params]))}))
)

