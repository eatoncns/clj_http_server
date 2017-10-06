(ns http-server.response
  (:require [http-server.utils.bytes :as bytes])
  (:import [java.io ByteArrayOutputStream]))

(defrecord Response [status headers body])

(def reasons { 200 "OK"
               404 "Not Found"})

(def crlf "\r\n")

(defn- build-status-line [status]
 (str "HTTP/1.1 " status " " (get reasons status) crlf))

(defn build
  [response]
  (let [{:keys [status body]} response]
    (->> (ByteArrayOutputStream.)
         (bytes/write (build-status-line status))
         (bytes/write crlf)
         (bytes/write body)
         (.toByteArray))))
