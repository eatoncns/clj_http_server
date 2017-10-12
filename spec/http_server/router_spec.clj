(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.spec-helper :refer [rshould-be-a]]
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

(describe "route"

  (it "returns a Tea for GET to /tea"
    (-> (build-request "GET" "/tea")
        (route "directory-served")
        (rshould-be-a Tea)))

  (it "returns a Coffee for GET to /coffee"
    (-> (build-request "GET" "/coffee")
        (route "directory-served")
        (rshould-be-a Coffee)))

  (it "returns a Cookie for GET to /cookie"
    (-> (build-request "GET" "/cookie")
        (route "directory-served")
        (rshould-be-a Cookie)))

  (it "returns a EatCookie for GET to /eat_cookie"
    (-> (build-request "GET" "/eat_cookie")
        (route "directory-served")
        (rshould-be-a EatCookie)))

  (it "returns a Parameters for GET to /parameters"
    (-> (build-request "GET" "/parameters")
        (route "directory-served")
        (rshould-be-a Parameters)))

  (it "returns a Form for GET to /form"
    (-> (build-request "GET" "/form")
        (route "directory-served")
        (rshould-be-a Form)))

  (it "returns a Patch for PATCH request"
    (-> (build-request "PATCH" "/foo")
        (route "directory-served")
        (rshould-be-a Patch)))

  (it "returns a Redirect for GET to /redirect"
    (-> (build-request "GET" "/redirect")
        (route "directory-served")
        (rshould-be-a Redirect)))

  (it "returns a MethodOptions for OPTIONS to /method_options"
    (-> (build-request "OPTIONS" "/method_options")
        (route "directory-served")
        (rshould-be-a MethodOptions)))

  (it "returns a MethodOptions2 for OPTIONS to /method_options2"
    (-> (build-request "OPTIONS" "/method_options2")
        (route "directory-served")
        (rshould-be-a MethodOptions2)))

  (it "returns a Logs for GET to /logs"
    (-> (build-request "GET" "/logs")
        (route "directory-served")
        (rshould-be-a Logs)))

  (it "returns NotAuthorised for unauthorised route"
    (-> (build-request "GET" "/whatver")
        (assoc :authorised false)
        (route "directory-served")
        (rshould-be-a NotAuthorised)))

  (it "returns a DefaultGET for GET request"
    (-> (build-request "GET" "/whatever")
        (route "directory-served")
        (rshould-be-a DefaultGET)))

  (it "returns MethodNotAllowed for other requests"
    (-> (build-request "POST" "/whatever")
        (route "directory-served")
        (rshould-be-a MethodNotAllowed)))
)

(describe "allowed-methods"

  (it "returns list of allowed methods for a uri"
    (-> (build-request "PATCH" "/form")
        (allowed-methods "directory-served")
        (should= ["DELETE" "GET" "PATCH" "POST" "PUT"])))
)
