(ns http-server.routes.method-not-allowed
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [clojure.string :as string]))

(defn- append-method [current method]
  (str current method ", "))

(defn- remove-trailing-comma [input]
  (apply str (drop-last input)))

(defn- build-header [allow-string]
  {"Allow" allow-string})

(defn- allowed-header [allowed]
  (->> allowed
       (reduce append-method "")
       (string/trim)
       (remove-trailing-comma)
       (build-header)))

(defrecord MethodNotAllowed [request allowed]
  route/Route
  (is-applicable? [this directory-served]
    true)

  (process [this directory-served]
    (map->Response {:status 405 :headers (allowed-header allowed) :body nil}))
)

