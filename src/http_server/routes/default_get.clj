(ns http-server.routes.default-get
  (:require [http-server.response]
            [http-server.utils.file-info :as fi]
            [http-server.routes.route :as route])
  (:import [http_server.response Response]
           [http_server.utils.file_info FileInfoAtRoot]))

(defn process-get [file-info, path]
  (if (fi/file-exists? file-info path)
    (Response. 200 (fi/file-data file-info path))
    (Response. 404 nil)))

(defrecord DefaultGET [request]
  route/Route
  (is-applicable [this]
    (= (get-in this [:request :method]) "GET"))

  (process [this directory-served]
    (process-get (FileInfoAtRoot. directory-served) (get-in this [:request :uri]))))
