(ns http-server.routes.parameters-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.parameters :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer [rshould=]]))

(describe "is-applicable"
  (it "returns true for GET to /parameters"
    (-> (map->Parameters{:request {:method "GET" :uri "/parameters"}})
        (route/is-applicable)
        (rshould= true)))

  (it "returns false for GET to other uri"
    (-> (map->Parameters{:request {:method "GET" :uri "/parameter"}})
        (route/is-applicable)
        (rshould= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->Parameters{:request {:method method :uri "/parameters"}})
          (route/is-applicable)
          (rshould= false))))
)

(describe "process"
  (it "returns 200 status"
    (-> (map->Parameters{:request {:method "GET" :uri "/parameters"}})
        (route/process "directory-served")
        (get :status)
        (rshould= 200)))

  (it "returns parameters from request header in body"
    (-> (map->Parameters{:request {:method "GET"
                                   :uri "/parameters"
                                   :params {"variable_1" "blah", "variable_2" "woah"}}})
        (route/process "directory-served")
        (get :body)
        (rshould= "variable_1 = blah\r\nvariable_2 = woah\r\n")))
)

