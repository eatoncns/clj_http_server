(ns http-server.response
  (:require [http-server.utils.bytes :as bytes]
            [http-server.constants.reasons :refer [reasons]]
            [clojure.string :as s])
  (:import [java.io ByteArrayOutputStream]))

(defrecord Response [status headers body])

(def content-types {"html" "text/html"
                    "txt" "text/plain"
                    "png" "image/png"
                    "jpeg" "image/jpeg"
                    "gif" "image/gif"})

(defn content-type [shorthand]
  {"Content-Type" (get content-types shorthand)})

(def crlf "\r\n")

(defn- build-status-line [status]
 (str "HTTP/1.1 " status " " (get reasons status) crlf))

(defn- add-header [current [k v]]
  (str current " " k ": " v))

(defn- build-headers [headers]
  (if (seq headers)
    (str (s/triml (reduce add-header "" (seq headers))) crlf)))

(defn build
  [response]
  (let [{:keys [status headers body]} response]
    (->> (ByteArrayOutputStream.)
         (bytes/write (build-status-line status))
         (bytes/write (build-headers headers))
         (bytes/write crlf)
         (bytes/write body)
         (.toByteArray))))
