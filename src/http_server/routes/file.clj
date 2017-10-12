(ns http-server.routes.file
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response content-type]]
            [http-server.utils.file-info :as fi]
            [http-server.utils.range :as rg]
            [http-server.utils.functional :refer [in?]]
            [http-server.constants.methods :refer :all])
  (:import [http_server.utils.file_info FileInfoAtRoot]))

(defn is-request-applicable [request file-info]
  (and (in? (get request :method) [GET HEAD])
       (fi/file-exists? file-info (get request :uri))))

(defn requests-range? [request]
  (let [headers (get request :headers)]
    (contains? headers "Range")))

(defn partial-file-data [file-info path request]
  (let [range-string (get-in request [:headers "Range"])
        [start length] (rg/calculate-range file-info path range-string)]
    (fi/partial-file-data file-info path start length)))

(defn process-request [request file-info]
  (let [path (get request :uri)]
    (if (requests-range? request)
      (map->Response {:status 206
                      :headers (content-type (fi/extension path))
                      :body (partial-file-data file-info path request)})
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
