(ns http-server.routes.parameters-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.parameters :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn parameters-request
  ([method uri]
    (parameters-request method uri {}))
  ([method uri params]
    (map->Parameters {:request {:method method :uri uri :params params}})))

(describe "is-applicable?"
  (it "returns true for GET to /parameters"
    (applicable-should= (parameters-request GET "/parameters") true))

  (it "returns false for GET to other uri"
    (applicable-should= (parameters-request GET "/parameter") false))

  (it "returns false for methods other than GET"
    (doseq [method (s/difference http-methods #{GET})]
      (applicable-should= (parameters-request method "/parameters") false)))
)

(describe "process"
  (it "returns 200 status"
    (-> (parameters-request GET "/parameters")
        (response-should-have :status 200)))

  (it "returns parameters from request header in body"
    (-> (parameters-request GET "/parameters" {"variable_1" "blah", "variable_2" "woah"})
        (response-should-have :body "variable_1 = blah\r\nvariable_2 = woah\r\n")))
)

