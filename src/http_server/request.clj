(ns http-server.request
  (:require [clojure.string :as str]))

(defrecord Request [method uri version headers])

(defn- parse-request-line [reader]
  (str/split (.readLine reader) #" " 3))

(defn- header-lines [reader]
  (take-while (complement empty?) (repeatedly #(.readLine reader))))

(defn- parse-header [headers header-line]
  (let [[k v] (str/split header-line #":" 2)]
    (assoc headers (str/trim k) (str/trim v))))

(defn- parse-headers [reader]
  (reduce parse-header {} (header-lines reader)))

(defn parse [reader]
  (let [[method uri version] (parse-request-line reader)
        headers (parse-headers reader)]
    (map->Request {:method method :uri uri :version version :headers headers})))
