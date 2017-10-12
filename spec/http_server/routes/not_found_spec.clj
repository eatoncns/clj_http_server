(ns http-server.not-found-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.not-found :refer :all]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn not-found-request [method uri]
  (map->NotFound {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for GET"
    (applicable-should= (not-found-request GET "/whatever") true))

  (it "returns true for HEAD"
    (applicable-should= (not-found-request HEAD "/whatever") true))

  (it "returns false for other methods"
    (doseq [method (s/difference http-methods #{GET HEAD})]
      (applicable-should= (not-found-request method "/whatever") false)))
)

(describe "process"
  (it "returns 404"
    (-> (not-found-request GET "/whatever")
        (response-should-have :status 404)))

  (it "returns no body"
    (-> (not-found-request GET "/whatever")
        (response-should-have :body nil)))
)
