(ns http-server.routes.cookie-spec
  (:require [speclj.core :refer :all]
            [http-server.spec-helper :refer :all]
            [http-server.routes.cookie :refer :all]
            [http-server.routes.route :as route]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn cookie-request [method uri]
  (map->Cookie {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for GET to /cookie"
    (applicable-should= (cookie-request GET "/cookie") true))

  (it "returns false for GET to other uri"
    (applicable-should= (cookie-request GET "/cooki") false))

  (it "returns false for methods other than GET"
    (doseq [method (s/difference http-methods #{GET})]
      (applicable-should= (cookie-request method "/cookie") false)))
)

(describe "process"
  (it "returns 200 status"
    (-> (cookie-request GET "/cookie")
        (response-should-have :status 200)))

  (it "returns eat message"
    (-> (cookie-request GET "/cookie")
        (response-should-have :body "Eat")))

  (it "returns Set-Cookie header"
    (-> (map->Cookie{:request {:method "GET" :uri "/cookie" :params {"type" "chocolate"}}})
        (route/process "directory-served")
        (get-in [:headers "Set-Cookie"])
        (rshould= "type=chocolate")))
)

