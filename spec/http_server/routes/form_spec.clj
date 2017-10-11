(ns http-server.routes.form-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.form :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer [rshould=]]))

(describe "is-applicable"
  (it "returns true for GET/POST/DELETE to /form"
    (doseq [method ["GET" "POST" "DELETE"]]
      (-> (map->Form{:request {:method method :uri "/form"}})
          (route/is-applicable)
          (rshould= true))))

  (it "returns false for other uris"
    (doseq [method ["GET" "POST" "DELETE"]]
      (-> (map->Form{:request {:method method :uri "/for"}})
          (route/is-applicable)
          (rshould= false))))

  (it "returns false for other methods"
    (doseq [method ["HEAD" "PUT" "OPTIONS"]]
      (-> (map->Form{:request {:method method :uri "/form"}})
          (route/is-applicable)
          (rshould= false))))
)

(describe "process-form"
  (it "returns 200 status for GET"
    (let [data (atom "data=fatcat")]
      (->> (process-form {:method "GET" :uri "/form"} data)
           (:status)
           (should= 200))))

  (it "returns current data in GET body"
    (let [data (atom "data=fatcat")]
      (->> (process-form {:method "GET" :uri "/form"} data)
           (:body)
           (should= "data=fatcat"))))

  (it "returns 200 status for POST"
    (let [data (atom "data=fatcat")]
      (->> (process-form {:method "POST" :uri "/form" :body "data=blah"} data)
           (:status)
           (should= 200))))

  (it "updates data with POST body"
    (let [data (atom "data=fatcat")]
      (process-form {:method "POST" :uri "/form" :body "data=blah"} data)
      (should= "data=blah" @data)))

  (it "returns 200 status for DELETE"
    (let [data (atom "data=fatcat")]
      (->> (process-form {:method "DELETE" :uri "/form"} data)
           (:status)
           (should= 200))))

  (it "resets data with DELETE"
    (let [data (atom "data=fatcat")]
      (process-form {:method "DELETE" :uri "/form"} data)
      (should= nil @data)))
)

