(ns http-server.routes.method-options-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.method-options :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable"
  (it "returns true for any method to /method_options"
    (doseq [method ["POST" "HEAD" "PUT" "GET" "OPTIONS"]]
      (-> (map->MethodOptions{:request {:method method :uri "/method_options"}})
          (route/is-applicable)
          (should= true))))

  (it "returns false for any method to other uri"
    (doseq [method ["POST" "HEAD" "PUT" "GET" "OPTIONS"]]
      (-> (map->MethodOptions{:request {:method method :uri "/met"}})
          (route/is-applicable)
          (should= false))))
)

(describe "process"
  (it "returns 200 status for all methods"
    (doseq [method ["POST" "HEAD" "PUT" "GET" "OPTIONS"]]
      (-> (map->MethodOptions{:request {:method method :uri "/method_options"}})
          (route/process "directory-served")
          (get :status)
          (should= 200))))

  (it "returns header allowing all methods for OPTION"
    (-> (map->MethodOptions{:request {:method "OPTION" :uri "/method_options"}})
        (route/process "directory-served")
        (get-in [:headers "Allow"])
        (should= "GET,HEAD,POST,OPTIONS,PUT")))
)

