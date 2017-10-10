(ns http-server.request
  (:require [http-server.utils.functional :as func]
            [clojure.string :as string])
  (:import [java.net URLDecoder]))

(defrecord Request [method uri version headers params])

(defn- parse-request-line [reader]
  (string/split (.readLine reader) #" " 3))

(defn- header-lines [reader]
  (take-while (complement empty?) (repeatedly #(.readLine reader))))

(defn- parse-header [headers header-line]
  (func/split-map headers header-line #":"))

(defn- parse-headers [reader]
  (reduce parse-header {} (header-lines reader)))

(defn trim-and-decode [s]
  (-> s
      (string/trim)
      (java.net.URLDecoder/decode "UTF-8")))

(defn- parse-param [params param-string]
  (func/split-map params param-string #"=" trim-and-decode))

(defn- parse-params [params-string]
  (if (nil? params-string)
    nil
    (reduce parse-param {} (string/split params-string #"&"))))

(defn- parse-full-uri [full-uri]
  (let [[uri params-string] (string/split full-uri #"\?" 2)
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
