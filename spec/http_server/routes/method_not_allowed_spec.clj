(ns http-server.routes.method-not-allowed-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.method-not-allowed :refer :all]
            [http-server.routes.route :as route]
            [http-server.spec-helper :refer [rshould=]]))

(describe "is-applicable?"

  (it "returns true for any request"
    (doseq [method ["GET" "POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->MethodNotAllowed{:request {:method method :uri "/whatever"}
                                 :allowed ["GET" "POST"]})
          (route/is-applicable? "directory-served")
          (rshould= true))))
)

(describe "process"
  (it "returns 405 status"
    (-> (map->MethodNotAllowed{:request {:method "GET" :uri "/whatever"}
                               :allowed ["GET" "POST"]})
        (route/process "directory-served")
        (get :status)
        (rshould= 405)))

  (it "returns no body"
    (-> (map->MethodNotAllowed{:request {:method "GET" :uri "/whatever"}
                               :allowed ["GET" "POST"]})
        (route/process "directory-served")
        (get :body)
        (rshould= nil)))

  (it "returns Allow header"
    (-> (map->MethodNotAllowed{:request {:method "GET" :uri "/whatever"}
                               :allowed ["GET" "POST"]})
        (route/process "directory-served")
        (get-in [:headers "Allow"])
        (rshould= "GET, POST")))
)

