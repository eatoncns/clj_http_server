(ns http-server.routes.tea-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.tea :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable"
  (it "returns true for GET to /tea"
    (-> (map->Tea{:request {:method "GET" :uri "/tea"}})
        (route/is-applicable)
        (should= true)))

  (it "returns false for GET to other uri"
    (-> (map->Tea{:request {:method "GET" :uri "/teaf"}})
        (route/is-applicable)
        (should= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->Tea{:request {:method method :uri "/tea"}})
          (route/is-applicable)
          (should= false))))
)

(describe "process"
  (it "returns 200 status"
    (-> (map->Tea{:request {:method "GET" :uri "/tea"}})
        (route/process "directory-served")
        (get :status)
        (should= 200)))

  (it "returns no body"
    (-> (map->Tea{:request {:method "GET" :uri "/tea"}})
        (route/process "directory-served")
        (get :body)
        (should= nil)))
)
