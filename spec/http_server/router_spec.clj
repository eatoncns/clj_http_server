(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.utils.functional :as func]
            [http-server.router :refer :all]
            [http-server.routes.default-get]
            [http-server.routes.coffee]
            [http-server.routes.tea]
            [http-server.routes.cookie]
            [http-server.routes.eat-cookie]
            [http-server.routes.parameters]
            [http-server.routes.form]
            [http-server.routes.patch]
            [http-server.routes.redirect]
            [http-server.routes.method-options]
            [http-server.routes.method-options2]
            [http-server.routes.logs]
            [http-server.routes.not-authorised]
            [http-server.request :refer [map->Request]])
  (:import [http_server.routes.default_get DefaultGET]
           [http_server.routes.coffee Coffee]
           [http_server.routes.tea Tea]
           [http_server.routes.cookie Cookie]
           [http_server.routes.eat_cookie EatCookie]
           [http_server.routes.parameters Parameters]
           [http_server.routes.form Form]
           [http_server.routes.patch Patch]
           [http_server.routes.redirect Redirect]
           [http_server.routes.method_options MethodOptions]
           [http_server.routes.method_options2 MethodOptions2]
           [http_server.routes.logs Logs]
           [http_server.routes.not_authorised NotAuthorised]
           [http_server.request Request]))

(defn build-request [method uri]
  (map->Request {:method method :uri uri :version "HTTP/1.1" :headers {} :authorised true}))

(describe "route"

  (it "returns a Tea for GET to /tea"
    (->> (build-request "GET" "/tea")
         (route)
         (should-be-a Tea)))

  (it "returns a Coffee for GET to /coffee"
    (->> (build-request "GET" "/coffee")
         (route)
         (should-be-a Coffee)))

  (it "returns a Cookie for GET to /cookie"
    (->> (build-request "GET" "/cookie")
         (route)
         (should-be-a Cookie)))

  (it "returns a EatCookie for GET to /eat_cookie"
    (->> (build-request "GET" "/eat_cookie")
         (route)
         (should-be-a EatCookie)))

  (it "returns a Parameters for GET to /parameters"
    (->> (build-request "GET" "/parameters")
         (route)
         (should-be-a Parameters)))

  (it "returns a Form for GET to /form"
    (->> (build-request "GET" "/form")
         (route)
         (should-be-a Form)))

  (it "returns a Patch for PATCH request"
    (->> (build-request "PATCH" "/foo")
         (route)
         (should-be-a Patch)))

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

  (it "returns a Logs for GET to /logs"
    (->> (build-request "GET" "/logs")
         (route)
         (should-be-a Logs)))

  (it "returns NotAuthorised for unauthorised route"
    (->> (build-request "GET" "/whatver")
         ((func/flip assoc) false :authorised)
         (route)
         (should-be-a NotAuthorised)))

  (it "returns a DefaultGET for GET request"
    (->> (build-request "GET" "/whatever")
         (route)
         (should-be-a DefaultGET)))

)
