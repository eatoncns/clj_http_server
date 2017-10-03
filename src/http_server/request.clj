(ns http-server.request
  (:require [clojure.string :as str]))

(defrecord Request [method uri])

(defn parse [input]
  (let [[request-line _] (str/split input #"\r\n" 2)
        [method uri version] (str/split request-line #" " 3)]
    (map->Request {:method method :uri uri})))

(defn process [request]
  "HTTP/1.1 200 OK\r\n\r\nHello World")
