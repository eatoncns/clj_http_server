(ns http-server.processor
  (:require [http-server.response]
            [http-server.file-info :as fi])
  (:import [http_server.response Response]
           [http_server.file_info FileInfoAtRoot]))

(defn process-get [file-info, path]
  (if (fi/file-exists? file-info path)
    (Response. 200 (fi/file-data file-info path))
    (Response. 404 nil)))

(defn process [request directory-served]
  (if (not= (:uri request) "/")
    (process-get (FileInfoAtRoot. directory-served) (:uri request))
    (Response. 200 (.getBytes "Hello World"))))
