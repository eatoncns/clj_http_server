(ns http-server.constants.errors
  (:require [http-server.response :refer [map->Response]]))

(defn error [code] (map->Response {:status code :headers {} :body nil}))

(def unprocessable-request (error 422))
(def precondition-failed (error 412))
(def file-not-found (error 404))
