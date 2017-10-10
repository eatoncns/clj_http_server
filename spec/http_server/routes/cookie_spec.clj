(ns http-server.routes.cookie-spec
  (:require [speclj.core :refer :all]
            [http-server.spec-helper :refer [rshould=]]
            [http-server.routes.cookie :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable"
  (it "returns true for GET to /cookie"
    (-> (map->Cookie{:request {:method "GET" :uri "/cookie"}})
        (route/is-applicable)
        (rshould= true)))

  (it "returns false for GET to other uri"
    (-> (map->Cookie{:request {:method "GET" :uri "/cooki"}})
        (route/is-applicable)
        (rshould= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->Cookie{:request {:method method :uri "/cookie"}})
          (route/is-applicable)
          (rshould= false))))
)

(describe "process"
  (it "returns 200 status"
    (-> (map->Cookie{:request {:method "GET" :uri "/cookie"}})
        (route/process "directory-served")
        (get :status)
        (rshould= 200)))

  (it "returns eat message"
    (-> (map->Cookie{:request {:method "GET" :uri "/cookie"}})
        (route/process "directory-served")
        (get :body)
        (rshould= "Eat")))

  (it "returns Set-Cookie header"
    (-> (map->Cookie{:request {:method "GET" :uri "/cookie" :params {"type" "chocolate"}}})
        (route/process "directory-served")
        (get-in [:headers "Set-Cookie"])
        (rshould= "type=chocolate")))
)

