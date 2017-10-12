(ns http-server.routes.form
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.utils.functional :refer [in?]])
  (:import [http_server.response Response]))

(def data (atom nil))

(defn- handle-get [current-data]
  (map->Response {:status 200 :headers {} :body @current-data}))

(defn- handle-update [request current-data]
  (reset! current-data (get request :body))
  (map->Response {:status 200 :headers {} :body nil}))

(defn- handle-delete [current-data]
  (reset! current-data nil)
  (map->Response {:status 200 :headers {} :body nil}))

(defn process-form [request current-data]
  (case (get request :method)
    "GET" (handle-get current-data)
    ("POST" "PUT") (handle-update request current-data)
    "DELETE" (handle-delete current-data)))

(defrecord Form [request]
  route/Route
  (is-applicable? [this directory-served]
    (and (= (get-in this [:request :uri]) "/form")
         (in? (get-in this [:request :method]) ["GET" "POST" "PUT" "DELETE"])))

  (process [this directory-served]
    (process-form (get this :request) data))
)

