(ns http-server.routes.eat-cookie
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.utils.functional :as func]
            [clojure.string :as string])
  (:import [http_server.response Response]))

(defn- parse-cookie [cookie-map cookie-string]
  (func/split-map cookie-map cookie-string #"="))

(defn- cookie-type-from-header [cookie-header]
  (let [cookie-strings (string/split cookie-header #";")
        cookie-map (reduce parse-cookie {} cookie-strings)]
    (if (contains? cookie-map "type")
      (get cookie-map "type")
      "")))

(defn- cookie-type [headers]
  (if (contains? headers "Cookie")
    (cookie-type-from-header (get headers "Cookie"))
    ""))

(defrecord EatCookie [request]
  route/Route
  (is-applicable [this]
    (and (= (get-in this [:request :uri]) "/eat_cookie")
         (= (get-in this [:request :method]) "GET")))

  (process [this directory-served]
    (map->Response {:status 200
                    :headers {}
                    :body (str "mmmm " (cookie-type (get-in this [:request :headers])))}))
)