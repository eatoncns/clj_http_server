(ns http-server.routes.redirect-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.redirect :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn redirect-request [method uri]
  (map->Redirect {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for GET to /redirect"
    (applicable-should= (redirect-request GET "/redirect") true))

  (it "returns false for GET to other uri"
    (applicable-should= (redirect-request GET "/redirec") false))

  (it "returns false for methods other than GET"
    (doseq [method (s/difference http-methods #{GET})]
      (applicable-should= (redirect-request method "/redirect") false)))
)

(describe "process"
  (it "returns 302 status"
    (-> (redirect-request GET "/redirect")
        (response-should-have :status 302)))

  (it "returns header with location /"
    (-> (redirect-request GET "/redirect")
        (response-should-have-header "Location" "/")))

  (it "returns no body"
    (-> (redirect-request GET "/redirect")
        (response-should-have :body nil)))
)
