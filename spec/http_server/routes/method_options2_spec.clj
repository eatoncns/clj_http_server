(ns http-server.routes.method-options2-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.method-options2 :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]))

(defn method-options2-request [method uri]
  (map->MethodOptions2 {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for allowed methods to /method_options2"
    (doseq [method [GET OPTIONS]]
      (applicable-should= (method-options2-request method "/method_options2") true)))

  (it "returns false for any method to other uri"
    (doseq [method http-methods]
      (applicable-should= (method-options2-request method "/met") false)))
)

(describe "process"
  (it "returns 200 status for allowed methods"
    (doseq [method [GET OPTIONS]]
      (-> (method-options2-request method "/method_options2")
          (response-should-have :status 200))))

  (it "returns header allowing GET/OPTIONS methods for OPTION"
    (-> (method-options2-request OPTIONS "/method_options2")
        (response-should-have-header "Allow" "GET,OPTIONS")))
)
