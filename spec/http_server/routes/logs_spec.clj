(ns http-server.routes.logs-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.logs :refer :all]
            [http-server.routes.route :as route]))

(describe "is-applicable?"
  (it "returns true for GET to /logs"
    (-> (map->Logs{:request {:method "GET" :uri "/logs"}})
        (route/is-applicable? "directory-served")
        (should= true)))

  (it "returns false for GET to other uri"
    (-> (map->Logs{:request {:method "GET" :uri "/log"}})
        (route/is-applicable? "directory-served")
        (should= false)))

  (it "returns false for methods other than GET"
    (doseq [method ["POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->Logs{:request {:method method :uri "/logs"}})
          (route/is-applicable? "directory-served")
          (should= false))))
)
