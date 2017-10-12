(ns http-server.routes.form-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.form :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer [applicable-should=]]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(def applicable-methods #{GET POST PUT DELETE})

(defn form-request
  ([method uri]
    (form-request method uri nil))
  ([method uri body]
    (map->Form {:request {:method method :uri uri :body body}})))

(defn process-form-should-have [method uri data field expected]
  (->> (process-form {:method method :uri uri} data)
       (field)
       (should= expected)))

(describe "is-applicable?"
  (it "returns true for GET/POST/PUT/DELETE to /form"
    (doseq [method applicable-methods]
      (applicable-should= (form-request method "/form") true)))

  (it "returns false for other uris"
    (doseq [method applicable-methods]
      (applicable-should= (form-request method "/for") false)))

  (it "returns false for other methods"
    (doseq [method (s/difference http-methods applicable-methods)]
      (applicable-should= (form-request method "/form") false)))
)

(describe "process-form"
  (it "returns 200 status for GET"
    (let [data (atom "data=fatcat")]
      (process-form-should-have GET "/form" data :status 200)))

  (it "returns current data in GET body"
    (let [data (atom "data=fatcat")]
      (process-form-should-have GET "/form" data :body "data=fatcat")))

  (it "returns 200 status for PUT/POST"
    (doseq [method [PUT POST]]
      (let [data (atom "data=fatcat")]
        (process-form-should-have method "/form" data :status 200))))

  (it "updates data with PUT/POST body"
    (doseq [method [PUT POST]]
      (let [data (atom "data=fatcat")]
        (process-form {:method method :uri "/form" :body "data=blah"} data)
        (should= "data=blah" @data))))

  (it "returns 200 status for DELETE"
    (let [data (atom "data=fatcat")]
      (process-form-should-have DELETE "/form" data :status 200)))

  (it "resets data with DELETE"
    (let [data (atom "data=fatcat")]
      (process-form {:method DELETE :uri "/form"} data)
      (should= nil @data)))
)
