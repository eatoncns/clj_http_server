(ns http-server.routes.not-authorised-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.not-authorised :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable?"
  (it "returns true for unauthorised request"
    (-> (map->NotAuthorised{:request {:authorised false}})
        (route/is-applicable? "directory-served")
        (should= true)))

  (it "returns false for methods on authorised request"
    (-> (map->NotAuthorised{:request {:authorised true}})
        (route/is-applicable? "directory-served")
        (should= false)))
)

(describe "process"
  (it "returns 401 status"
    (-> (map->NotAuthorised{:request {:method "GET" :uri "/coffee"}})
        (route/process "directory-served")
        (get :status)
        (should= 401)))

  (it "sets authentication header"
    (-> (map->NotAuthorised{:request {:method "GET" :uri "/coffee"}})
        (route/process "directory-served")
        (get-in [:headers "WWW-Authenticate"])
        (should= "Basic realm=authentication")))

  (it "returns no body"
    (-> (map->NotAuthorised{:request {:method "GET" :uri "/coffee"}})
        (route/process "directory-served")
        (get :body)
        (should= nil)))
)

