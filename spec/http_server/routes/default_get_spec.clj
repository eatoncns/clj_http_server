(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.default-get :refer :all]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn default-get-request [method uri]
  (map->DefaultGET {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for GET"
    (applicable-should= (default-get-request GET "/whatever") true))

  (it "returns true for HEAD"
    (applicable-should= (default-get-request HEAD "/whatever") true))

  (it "returns false for other methods"
    (doseq [method (s/difference http-methods #{GET HEAD})]
      (applicable-should= (default-get-request method "/whatever") false)))
)

(describe "process"
  (it "returns 404"
    (-> (default-get-request GET "/whatever")
        (response-should-have :status 404)))

  (it "returns no body"
    (-> (default-get-request GET "/whatever")
        (response-should-have :body nil)))
)
