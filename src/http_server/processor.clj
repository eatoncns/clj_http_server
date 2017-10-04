(ns http-server.processor
  (:require [http-server.response]
            [http-server.file-info :as fi])
  (:import [http_server.response Response]))

(defn process-get [file-info, path]
  (if (fi/file-exists? file-info path)
    (Response. 200 nil)
    (Response. 404 nil)))

(defn process [request directory-served]
  (Response. 200 "Hello World"))
