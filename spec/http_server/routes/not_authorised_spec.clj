(ns http-server.routes.not-authorised-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.not-authorised :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]))

(defn not-authorised-request [authorised]
  (map->NotAuthorised {:request {:authorised authorised}}))

(describe "is-applicable?"
  (it "returns true for unauthorised request"
    (applicable-should= (not-authorised-request false) true))

  (it "returns false for methods on authorised request"
    (applicable-should= (not-authorised-request true) false))
)

(describe "process"
  (it "returns 401 status"
    (-> (not-authorised-request false)
        (response-should-have :status 401)))

  (it "sets authentication header"
    (-> (not-authorised-request false)
        (response-should-have-header "WWW-Authenticate" "Basic realm=authentication")))

  (it "returns no body"
    (-> (not-authorised-request false)
        (response-should-have :body nil)))
)

