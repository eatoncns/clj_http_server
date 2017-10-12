(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.spec-helper :refer [rshould-be-a]]
            [http-server.router :refer :all]
            [http-server.constants.methods :refer :all]
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
            [http-server.routes.method-not-allowed]
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
           [http_server.routes.method_not_allowed MethodNotAllowed]
           [http_server.request Request]))

(defn build-request [method uri]
  (map->Request {:method method :uri uri :version "HTTP/1.1" :headers {} :authorised true}))

(defn route-should-be-a [method uri expected]
  (-> (build-request method uri)
      (route "directory-served")
      (rshould-be-a expected)))

(describe "route"

  (it "returns a Tea for GET to /tea"
    (route-should-be-a GET "/tea" Tea))

  (it "returns a Coffee for GET to /coffee"
    (route-should-be-a GET "/coffee" Coffee))

  (it "returns a Cookie for GET to /cookie"
    (route-should-be-a GET "/cookie" Cookie))

  (it "returns a EatCookie for GET to /eat_cookie"
    (route-should-be-a GET "/eat_cookie" EatCookie))

  (it "returns a Parameters for GET to /parameters"
    (route-should-be-a GET "/parameters" Parameters))

  (it "returns a Form for GET to /form"
    (route-should-be-a GET "/form" Form))

  (it "returns a Patch for PATCH request"
    (route-should-be-a PATCH "/foo" Patch))

  (it "returns a Redirect for GET to /redirect"
    (route-should-be-a GET "/redirect" Redirect))

  (it "returns a MethodOptions for OPTIONS to /method_options"
    (route-should-be-a OPTIONS "/method_options" MethodOptions))

  (it "returns a MethodOptions2 for OPTIONS to /method_options2"
    (route-should-be-a OPTIONS "/method_options2" MethodOptions2))

  (it "returns a Logs for GET to /logs"
    (route-should-be-a GET "/logs" Logs))

  (it "returns a DefaultGET for GET request"
    (route-should-be-a GET "/whatever" DefaultGET))

  (it "returns MethodNotAllowed for other requests"
    (route-should-be-a POST "/whatever" MethodNotAllowed))

  (it "returns NotAuthorised for unauthorised route"
    (-> (build-request GET "/whatver")
        (assoc :authorised false)
        (route "directory-served")
        (rshould-be-a NotAuthorised)))
)

(describe "allowed-methods"

  (it "returns list of allowed methods for a uri"
    (-> (build-request "PATCH" "/form")
        (allowed-methods "directory-served")
        (should= ["DELETE" "GET" "PATCH" "POST" "PUT"])))
)
