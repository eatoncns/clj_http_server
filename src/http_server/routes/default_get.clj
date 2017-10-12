(ns http-server.routes.default-get
  (:require [http-server.response :refer [map->Response content-type]]
            [http-server.utils.file-info :as fi]
            [http-server.utils.html :as html]
            [http-server.utils.range :as rg]
            [http-server.routes.route :as route])
  (:import [http_server.response Response]
           [http_server.utils.file_info FileInfoAtRoot]))

(defn requests-range? [request]
  (let [headers (get request :headers)]
    (contains? headers "Range")))

(defn partial-file-data [file-info path request]
  (let [range-string (get-in request [:headers "Range"])
        [start length] (rg/calculate-range file-info path range-string)]
    (fi/partial-file-data file-info path start length)))

(defn process-get [request file-info]
  (let [path (get request :uri)]
    (cond
      (fi/is-directory? file-info path)
        (map->Response {:status 200
                        :headers (content-type "html")
                        :body (html/list-of-links (fi/list-files file-info path))})
      (and (fi/file-exists? file-info path) (requests-range? request))
        (map->Response {:status 206
                        :headers (content-type (fi/extension path))
                        :body (partial-file-data file-info path request)})
      (fi/file-exists? file-info path)
        (map->Response {:status 200
                        :headers (content-type (fi/extension path))
                        :body (fi/file-data file-info path)})
      :else
        (map->Response {:status 404
                        :headers {}
                        :body nil}))))

(defrecord DefaultGET [request]
  route/Route
  (is-applicable? [this directory-served]
    (= (get-in this [:request :method]) "GET"))

  (process [this directory-served]
    (process-get (get this :request) (FileInfoAtRoot. directory-served))))
