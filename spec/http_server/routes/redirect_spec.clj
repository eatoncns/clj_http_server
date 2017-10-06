(ns http-server.routes.redirect-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.redirect :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable"
  (it "returns true for GET to /redirect"
    (-> (map->Redirect{:request {:method "GET" :uri "/redirect"}})
        (route/is-applicable)
        (should= true)))

  (it "returns false for GET to other uri"
    (-> (map->Redirect{:request {:method "GET" :uri "/redire"}})
        (route/is-applicable)
        (should= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTION"]]
      (-> (map->Redirect{:request {:method method :uri "/redirect"}})
          (route/is-applicable)
          (should= false))))
)

(describe "process"
  (it "returns 302 status"
    (-> (map->Redirect{:request {:method "GET" :uri "/redirect"}})
        (route/process "directory-served")
        (get :status)
        (should= 302)))

  (it "returns header with location /"
    (-> (map->Redirect{:request {:method "GET" :uri "/redirect"}})
        (route/process "directory-served")
        (get-in [:headers "Location"])
        (should= "/")))

  (it "returns no body"
    (-> (map->Redirect{:request {:method "GET" :uri "/redirect"}})
        (route/process "directory-served")
        (get :body)
        (should= nil)))
)
