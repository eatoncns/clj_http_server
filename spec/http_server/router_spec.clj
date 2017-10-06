(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.router :refer :all]
            [http-server.routes.default-get]
            [http-server.routes.default]
            [http-server.routes.coffee]
            [http-server.routes.tea]
            [http-server.routes.redirect]
            [http-server.routes.method-options]
            [http-server.request])
  (:import [http_server.routes.default_get DefaultGET]
           [http_server.routes.default Default]
           [http_server.routes.coffee Coffee]
           [http_server.routes.tea Tea]
           [http_server.routes.redirect Redirect]
           [http_server.routes.method_options MethodOptions]
           [http_server.request Request]))

(describe "route"

  (it "returns a Tea for GET to /tea"
    (->> (Request. "GET" "/tea")
         (route)
         (should-be-a Tea)))

  (it "returns a Coffee for GET to /coffee"
    (->> (Request. "GET" "/coffee")
         (route)
         (should-be-a Coffee)))

  (it "returns a Redirect for GET to /redirect"
    (->> (Request. "GET" "/redirect")
         (route)
         (should-be-a Redirect)))

  (it "returns a MethodOptions for OPTIONS to /method_options"
    (->> (Request. "OPTIONS" "/method_options")
         (route)
         (should-be-a MethodOptions)))

  (it "returns a DefaultGET for GET request"
    (->> (Request. "GET" "/whatever")
         (route)
         (should-be-a DefaultGET)))


  (it "returns a Default for other requests"
    (->> (Request. "POST" "/whatever")
         (route)
         (should-be-a Default)))
)

