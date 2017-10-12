(ns http-server.routes.tea-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.tea :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer :all]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn tea-request [method uri]
  (map->Tea {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for GET to /tea"
    (applicable-should= (tea-request GET "/tea") true))

  (it "returns false for GET to other uri"
    (applicable-should= (tea-request GET "/teaf") false))

  (it "returns false for methods other than GET"
    (doseq [method (s/difference http-methods #{GET})]
      (applicable-should= (tea-request method "/tea") false)))
)

(describe "process"
  (it "returns 200 status"
    (-> (tea-request GET "/tea")
        (response-should-have :status 200)))

  (it "returns no body"
    (-> (tea-request GET "/tea")
        (response-should-have :body nil)))
)
