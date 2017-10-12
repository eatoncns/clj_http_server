(ns http-server.routes.method-not-allowed-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.method-not-allowed :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]))

(defn method-not-allowed-request [method uri allowed]
  (map->MethodNotAllowed {:request {:method method :uri uri}
                          :allowed allowed}))

(describe "is-applicable?"

  (it "returns true for any request"
    (doseq [method http-methods]
      (applicable-should= (method-not-allowed-request method "/whatever" [GET POST]) true)))
)

(describe "process"
  (it "returns 405 status"
    (-> (method-not-allowed-request GET "/whatever" [GET POST])
        (response-should-have :status 405)))

  (it "returns no body"
    (-> (method-not-allowed-request GET "/whatever" [GET POST])
        (response-should-have :body nil)))

  (it "returns Allow header"
    (-> (method-not-allowed-request GET "/whatever" [GET POST])
        (response-should-have-header "Allow" "GET, POST")))
)

