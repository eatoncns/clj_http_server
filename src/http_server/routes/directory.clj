(ns http-server.routes.directory
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.constants.content-type :refer [content-type]]
            [http-server.utils.functional :refer [in?]]
            [http-server.constants.methods :refer :all]
            [http-server.utils.file-info :as fi]
            [http-server.utils.html :as html])
  (:import [http_server.utils.file_info FileInfoAtRoot]))

(defn is-request-applicable [request file-info]
  (and (in? (get request :method) [GET HEAD])
       (fi/is-directory? file-info (get request :uri))))

(defn- build-body [request file-info]
  (let [method (get request :method)
        path (get request :uri)]
    (if (= method HEAD)
      nil
      (html/list-of-links (fi/list-files file-info path)))))

(defn process-request [request file-info]
  (map->Response {:status 200
                  :headers (content-type "html")
                  :body (build-body request file-info)}))

(defrecord Directory [request]
  route/Route
  (is-applicable? [this directory-served]
    (is-request-applicable (get this :request) (FileInfoAtRoot. directory-served)))

  (process [this directory-served]
    (process-request (get this :request) (FileInfoAtRoot. directory-served)))
)

