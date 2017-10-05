(ns http-server.router
  (:require [http-server.response]
            [http-server.file-info :as fi])
  (:import [http_server.response Response]
           [http_server.file_info FileInfoAtRoot]))

(defprotocol Route
  (is-applicable [this])
  (process [this directory-served]))

(defn process-get [file-info, path]
  (if (fi/file-exists? file-info path)
    (Response. 200 (fi/file-data file-info path))
    (Response. 404 nil)))

(defrecord DefaultGET [request]
  Route
  (is-applicable [this]
    (= (get-in this [:request :method]) "GET"))

  (process [this directory-served]
    (process-get (FileInfoAtRoot. directory-served) (:uri request))))

(defrecord Default [request]
  Route
  (is-applicable [this]
    true)

  (process [this _]
    (Response. 200 (.getBytes "Hello World"))))

(def route-constructors [->DefaultGET ->Default])

(defn route [request]
  (let [routes (map #(%1 request) route-constructors)]
    (first (filter is-applicable routes))))
