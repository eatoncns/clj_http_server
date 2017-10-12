(ns http-server.routes.eat-cookie-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.eat-cookie :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable?"
  (it "returns true for GET to /eat_cookie"
    (-> (map->EatCookie{:request {:method "GET" :uri "/eat_cookie"}})
        (route/is-applicable? "directory-served")
        (should= true)))

  (it "returns false for GET to other uri"
    (-> (map->EatCookie{:request {:method "GET" :uri "/eat_cooki"}})
        (route/is-applicable? "directory-served")
        (should= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->EatCookie{:request {:method method :uri "/eat_cookie"}})
          (route/is-applicable? "directory-served")
          (should= false))))
)

(describe "process"
  (it "returns 200 status"
    (-> (map->EatCookie{:request {:method "GET" :uri "/eat_cookie"}})
        (route/process "directory-served")
        (get :status)
        (should= 200)))

  (it "returns message based on header cookie"
    (-> (map->EatCookie{:request {:method "GET" :uri "/eat_cookie" :headers {"Cookie" "type=chocolate"}}})
        (route/process "directory-served")
        (get :body)
        (should= "mmmm chocolate")))
)

