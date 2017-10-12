(ns http-server.routes.coffee-spec
  (:require [speclj.core :refer :all]
            [http-server.spec-helper :refer :all]
            [http-server.routes.coffee :refer :all]
            [http-server.constants.methods :refer :all]
            [http-server.routes.route :as route]
            [clojure.set :as s]))

(defn coffee-request [method uri]
  (map->Coffee {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for GET to /coffee"
    (applicable-should= (coffee-request GET "/coffee") true))

  (it "returns false for GET to other uri"
    (applicable-should= (coffee-request GET "/cof") false))

  (it "returns false for methods other than GET"
    (doseq [method (s/difference http-methods #{GET})]
      (applicable-should= (coffee-request method "/coffee") false)))
)

(describe "process"
  (it "returns 418 status"
    (-> (coffee-request GET "/coffee")
        (response-should-have :status 418)))

  (it "returns teapot message"
    (-> (coffee-request GET "/coffee")
        (response-should-have :body "I'm a teapot")))
)
