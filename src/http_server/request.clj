(ns http-server.request
  (:require [clojure.string :as str]))

(defrecord Request [method uri version headers params])

(defn- split-map [coll line divider]
  (let [[k v] (str/split line divider 2)]
    (assoc coll (str/trim k) (str/trim v))))

(defn- parse-request-line [reader]
  (str/split (.readLine reader) #" " 3))

(defn- header-lines [reader]
  (take-while (complement empty?) (repeatedly #(.readLine reader))))

(defn- parse-header [headers header-line]
  (split-map headers header-line #":"))

(defn- parse-headers [reader]
  (reduce parse-header {} (header-lines reader)))

(defn- parse-param [params param-string]
  (split-map params param-string #"="))

(defn- parse-params [params-string]
  (if (nil? params-string)
    nil
    (reduce parse-param {} (str/split params-string #"&"))))

(defn- parse-full-uri [full-uri]
  (let [[uri params-string] (str/split full-uri #"\?" 2)
        params (parse-params params-string)]
    [uri params]))

(defn parse [reader]
  (let [[method full-uri version] (parse-request-line reader)
        [uri params] (parse-full-uri full-uri)
        headers (parse-headers reader)]
    (map->Request {:method method
                   :uri uri
                   :version version
                   :headers headers
                   :params params})))
