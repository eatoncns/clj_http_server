(ns http-server.routes.method-options2-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.method-options2 :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable"
  (it "returns true for allowed methods to /method_options2"
    (doseq [method ["GET" "OPTIONS"]]
      (-> (map->MethodOptions2{:request {:method method :uri "/method_options2"}})
          (route/is-applicable)
          (should= true))))

  (it "returns false for any method to other uri"
    (doseq [method ["POST" "HEAD" "PUT" "GET" "OPTIONS"]]
      (-> (map->MethodOptions2{:request {:method method :uri "/met"}})
          (route/is-applicable)
          (should= false))))
)

(describe "process"
  (it "returns 200 status for allowed methods"
    (doseq [method ["GET" "OPTIONS"]]
      (-> (map->MethodOptions2{:request {:method method :uri "/method_options2"}})
          (route/process "directory-served")
          (get :status)
          (should= 200))))

  (it "returns header allowing GET/OPTIONS methods for OPTION"
    (-> (map->MethodOptions2{:request {:method "OPTION" :uri "/method_options2"}})
        (route/process "directory-served")
        (get-in [:headers "Allow"])
        (should= "GET,OPTIONS")))
)
