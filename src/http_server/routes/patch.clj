(ns http-server.routes.patch
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.utils.file-info :as fi]
            [http-server.utils.sha1 :as sha1]
            [http-server.constants.errors :as err])
  (:import [http_server.response Response]
           [http_server.utils.file_info FileInfoAtRoot]))

(defn- do-patch [request file-info]
  (let [body (:body request)
        body-hash (sha1/encode-str body)
        uri (:uri request)]
    (fi/patch-file file-info uri body)
    (map->Response {:status 204 :headers {"ETag" body-hash} :body nil})))

(defn- process-if-match [request file-info]
  (let [uri (:uri request)
        request-hash (get-in request [:headers "If-Match"])
        current-hash (sha1/encode (fi/file-data file-info uri))]
    (if (= request-hash current-hash)
      (do-patch request file-info)
      err/precondition-failed)))

(defn- has-if-match? [request]
  (contains? (:headers request) "If-Match"))

(defn- check-if-match [request file-info]
  (if (has-if-match? request)
    (process-if-match request file-info)
    err/unprocessable-request))

(defn process-patch [request file-info]
  (let [uri (:uri request)]
    (if (fi/file-exists? file-info uri)
      (check-if-match request file-info)
      err/file-not-found)))

(defrecord Patch [request]
  route/Route
  (is-applicable? [this directory-served]
    (= (get-in this [:request :method]) "PATCH"))

  (process [this directory-served]
    (process-patch (get this :request) (FileInfoAtRoot. directory-served)))
)

