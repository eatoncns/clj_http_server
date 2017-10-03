(ns http-server.processor
  (:require [http-server.response])
  (:import [http_server.response Response]))

(defn process [request directory-served]
  (Response. 200 "Hello World"))
