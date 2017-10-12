(ns http-server.routes.eat-cookie-spec
  (:require [speclj.core :refer :all]
            [http-server.spec-helper :refer :all]
            [http-server.routes.eat-cookie :refer :all]
            [http-server.constants.methods :refer :all]
            [http-server.routes.route :as route]
            [clojure.set :as s]))

(defn eat-cookie-request
  ([method uri]
   (eat-cookie-request method uri {}))
  ([method uri headers]
    (map->EatCookie {:request {:method method :uri uri :headers headers}})))

(describe "is-applicable?"
  (it "returns true for GET to /eat_cookie"
    (applicable-should= (eat-cookie-request GET "/eat_cookie") true))

  (it "returns false for GET to other uri"
    (applicable-should= (eat-cookie-request GET "/eat_cooki") false))

  (it "returns false for methods other than GET"
    (doseq [method (s/difference http-methods #{GET})]
      (applicable-should= (eat-cookie-request method "/eat_cookie") false)))
)

(describe "process"
  (it "returns 200 status"
    (-> (eat-cookie-request GET "/eat_cookie")
        (response-should-have :status 200)))

  (it "returns message based on header cookie"
    (-> (eat-cookie-request GET "/eat_cookie" {"Cookie" "type=chocolate"})
        (response-should-have :body "mmmm chocolate")))
)

