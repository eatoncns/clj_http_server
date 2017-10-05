(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.router :refer :all]
            [http-server.routes.default-get]
            [http-server.routes.default]
            [http-server.request])
  (:import [http_server.routes.default_get DefaultGET]
           [http_server.routes.default Default]
           [http_server.request Request]))

(describe "route"
  (it "returns a DefaultGET for GET request"
    (->> (Request. "GET" "/whatever")
         (route)
         (should-be-a DefaultGET)))


  (it "returns a Default for other requests"
    (->> (Request. "POST" "/whatever")
         (route)
         (should-be-a Default)))
)

