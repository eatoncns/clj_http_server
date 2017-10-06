(ns http-server.routes.default-get
  (:require [http-server.response]
            [http-server.utils.file-info :as fi]
            [http-server.utils.html :as html]
            [http-server.routes.route :as route])
  (:import [http_server.response Response]
           [http_server.utils.file_info FileInfoAtRoot]))

(defn process-get [path, file-info]
  (cond
    (fi/is-directory? file-info path)
      (Response. 200 {"Content-Type" "text/html"} (html/list-of-links (fi/list-files file-info path)))
    (fi/file-exists? file-info path)
      (Response. 200 {} (fi/file-data file-info path))
    :else
      (Response. 404 {} nil)))

(defrecord DefaultGET [request]
  route/Route
  (is-applicable [this]
    (= (get-in this [:request :method]) "GET"))

  (process [this directory-served]
    (process-get (get-in this [:request :uri]) (FileInfoAtRoot. directory-served))))
