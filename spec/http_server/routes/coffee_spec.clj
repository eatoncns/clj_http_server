(ns http-server.routes.coffee-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.coffee :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable"
  (it "returns true for GET to /coffee"
    (-> (map->Coffee{:request {:method "GET" :uri "/coffee"}})
        (route/is-applicable)
        (should= true)))

  (it "returns false for GET to other uri"
    (-> (map->Coffee{:request {:method "GET" :uri "/cof"}})
        (route/is-applicable)
        (should= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTION"]]
      (-> (map->Coffee{:request {:method method :uri "/cof"}})
          (route/is-applicable)
          (should= false))))
)

(describe "process"
  (it "returns 418 status"
    (-> (map->Coffee{:request {:method "GET" :uri "/coffee"}})
        (route/process "directory-served")
        (get :status)
        (should= 418)))

  (it "returns teapot message"
    (-> (map->Coffee{:request {:method "GET" :uri "/coffee"}})
        (route/process "directory-served")
        (get :body)
        (should= "I'm a teapot")))
)
