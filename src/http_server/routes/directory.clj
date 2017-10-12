(ns http-server.routes.directory
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.utils.functional :refer [in?]]
            [http-server.constants.methods :refer :all]
            [http-server.utils.file-info :as fi])
  (:import [http_server.response Response]
           [http_server.utils.file_info FileInfoAtRoot]))

(defn is-request-applicable [request file-info]
  (and (in? (get request :method) [GET HEAD])
       (fi/is-directory? file-info (get request :uri))))

;(defrecord Directory [request]
;  route/Route
;  (is-applicable [this directory-served]
;    (is-request-applicable (get this :request) (FileInfoAtRoot. directory-served)))
;
;  (process [this directory-served]
;    (map->Response {:status 418 :headers {} :body "I'm a teapot"}))
;)

