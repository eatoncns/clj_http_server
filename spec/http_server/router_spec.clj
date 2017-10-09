(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.router :refer :all]
            [http-server.routes.default-get]
            [http-server.routes.default]
            [http-server.routes.coffee]
            [http-server.routes.tea]
            [http-server.routes.redirect]
            [http-server.routes.method-options]
            [http-server.routes.method-options2]
            [http-server.request :refer [map->Request]])
  (:import [http_server.routes.default_get DefaultGET]
           [http_server.routes.default Default]
           [http_server.routes.coffee Coffee]
           [http_server.routes.tea Tea]
           [http_server.routes.redirect Redirect]
           [http_server.routes.method_options MethodOptions]
           [http_server.routes.method_options2 MethodOptions2]
           [http_server.request Request]))

(defn build-request [method uri]
  (map->Request {:method method :uri uri :version "HTTP/1.1" :headers {}}))

(describe "route"

  (it "returns a Tea for GET to /tea"
    (->> (build-request "GET" "/tea")
         (route)
         (should-be-a Tea)))

  (it "returns a Coffee for GET to /coffee"
    (->> (build-request "GET" "/coffee")
         (route)
         (should-be-a Coffee)))

  (it "returns a Redirect for GET to /redirect"
    (->> (build-request "GET" "/redirect")
         (route)
         (should-be-a Redirect)))

  (it "returns a MethodOptions for OPTIONS to /method_options"
    (->> (build-request "OPTIONS" "/method_options")
         (route)
         (should-be-a MethodOptions)))

  (it "returns a MethodOptions2 for OPTIONS to /method_options2"
    (->> (build-request "OPTIONS" "/method_options2")
         (route)
         (should-be-a MethodOptions2)))

  (it "returns a DefaultGET for GET request"
    (->> (build-request "GET" "/whatever")
         (route)
         (should-be-a DefaultGET)))


  (it "returns a Default for other requests"
    (->> (build-request "POST" "/whatever")
         (route)
         (should-be-a Default)))
)

