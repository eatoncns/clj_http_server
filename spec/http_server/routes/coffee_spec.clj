(ns http-server.routes.coffee-spec
  (:require [speclj.core :refer :all]
            [http-server.spec-helper :refer [rshould=]]
            [http-server.routes.coffee :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable?"
  (it "returns true for GET to /coffee"
    (-> (map->Coffee{:request {:method "GET" :uri "/coffee"}})
        (route/is-applicable? "directory-served")
        (rshould= true)))

  (it "returns false for GET to other uri"
    (-> (map->Coffee{:request {:method "GET" :uri "/cof"}})
        (route/is-applicable? "directory-served")
        (rshould= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->Coffee{:request {:method method :uri "/coffee"}})
          (route/is-applicable? "directory-served")
          (rshould= false))))
)

(describe "process"
  (it "returns 418 status"
    (-> (map->Coffee{:request {:method "GET" :uri "/coffee"}})
        (route/process "directory-served")
        (get :status)
        (rshould= 418)))

  (it "returns teapot message"
    (-> (map->Coffee{:request {:method "GET" :uri "/coffee"}})
        (route/process "directory-served")
        (get :body)
        (rshould= "I'm a teapot")))
)
