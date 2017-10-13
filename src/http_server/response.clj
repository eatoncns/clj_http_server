(ns http-server.response
  (:require [http-server.utils.bytes :as bytes]
            [http-server.constants.reasons :refer [reasons]]
            [clojure.string :as s])
  (:import [java.io ByteArrayOutputStream]))

(defrecord Response [status headers body])

(def crlf "\r\n")

(defn- build-status-line [status]
 (str "HTTP/1.1 " status " " (get reasons status) crlf))

(defn- add-header [current [k v]]
  (str current k ": " v crlf))

(defn- get-content-length [body]
  (str (bytes/length body)))

(defn- add-content-length [headers body]
  (if (nil? body)
    headers
    (assoc headers "Content-Length" (get-content-length body))))

(defn- build-headers [headers body]
  (let [extended-headers (add-content-length headers body)]
    (if (seq extended-headers)
      (s/triml (reduce add-header "" (seq extended-headers))))))

(defn build
  [response]
  (let [{:keys [status headers body]} response]
    (->> (ByteArrayOutputStream.)
         (bytes/write (build-status-line status))
         (bytes/write (build-headers headers body))
         (bytes/write crlf)
         (bytes/write body)
         (.toByteArray))))
