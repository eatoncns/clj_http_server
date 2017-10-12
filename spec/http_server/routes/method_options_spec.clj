(ns http-server.routes.method-options-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.method-options :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]))

(def applicable-methods #{POST HEAD PUT GET OPTIONS})

(defn method-options-request [method uri]
  (map->MethodOptions {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for applicale method to /method_options"
    (doseq [method applicable-methods]
      (applicable-should= (method-options-request method "/method_options") true)))

  (it "returns false for any method to other uri"
    (doseq [method applicable-methods]
      (applicable-should= (method-options-request method "/met") false)))
)

(describe "process"
  (it "returns 200 status for applicable methods"
    (doseq [method applicable-methods]
      (-> (method-options-request method "/method_options")
          (response-should-have :status 200))))

  (it "returns header allowing all methods for OPTION"
    (-> (method-options-request OPTIONS "/method_options")
        (response-should-have-header "Allow" "GET,HEAD,POST,OPTIONS,PUT")))
)
