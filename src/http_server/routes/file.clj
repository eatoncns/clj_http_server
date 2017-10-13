(ns http-server.routes.file
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.constants.content-type :refer [content-type]]
            [http-server.utils.file-info :as fi]
            [http-server.utils.range :as rg]
            [http-server.utils.functional :refer [in?]]
            [http-server.constants.methods :refer :all]
            [clojure.string :as string])
  (:import [http_server.utils.file_info FileInfoAtRoot]))

(defn is-request-applicable [request file-info]
  (and (in? (get request :method) [GET HEAD])
       (fi/file-exists? file-info (get request :uri))))

(defn requests-range? [request]
  (let [headers (get request :headers)]
    (contains? headers "Range")))

(defn request-range [request]
  (get-in request [:headers "Range"]))

(defn partial-file-data [request file-info path]
  (let [range-string (request-range request)
        [start length] (rg/calculate-range file-info path range-string)]
    (fi/partial-file-data file-info path start length)))

(defn content-range-header [request file-info path]
  (let [range-string (string/replace (request-range request) #"=" " ")
        file-length (str (fi/file-length file-info path))
        content-range (str range-string "/" file-length)]
    {"Content-Range" content-range}))

(defn partial-headers [request file-info path]
  (merge (content-type (fi/extension path))
         (content-range-header request file-info path)))

(defn process-request [request file-info]
  (let [path (get request :uri)]
    (if (requests-range? request)
      (map->Response {:status 206
                      :headers (partial-headers request file-info path)
                      :body (partial-file-data request file-info path)})
      (map->Response {:status 200
                      :headers (content-type (fi/extension path))
                      :body (fi/file-data file-info path)}))))

(defrecord File [request]
  route/Route
  (is-applicable? [this directory-served]
    (is-request-applicable request (FileInfoAtRoot. directory-served)))

  (process [this directory-served]
    (process-request request (FileInfoAtRoot. directory-served)))
)
