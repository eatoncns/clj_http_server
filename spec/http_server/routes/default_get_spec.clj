(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.default-get :refer :all]
            [http-server.utils.file-info :as fi]
            [http-server.utils.functional :as func]
            [http-server.request :refer [map->Request]]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (file-exists? [this path] (= path "/foo.png"))
  (file-length [this path] 100)
  (file-data [this path] "blah")
  (partial-file-data [this path start length] (str path " " start " " length)))

(defn run-get [request]
  (->> (FakeFileInfo.)
       (process-get request)))

(defn response-should-have [route-instance field expected]
  (->> route-instance
       (run-get)
       (field)
       (should= expected)))

(describe "process-get"

  (it "returns 404 when path does not exist"
    (-> (map->Request {:uri "/foobar"})
        (response-should-have :status 404)))

  (it "returns 200 when path is a file that exists"
    (-> (map->Request {:uri "/foo.png"})
        (response-should-have :status 200)))

  (it "returns file contents as body when path is a file that exists"
    (-> (map->Request {:uri "/foo.png"})
        (response-should-have :body "blah")))

  (it "returns 206 for partial file content"
    (-> (map->Request {:uri "/foo.png" :headers {"Range" "bytes=0-4"}})
        (response-should-have :status 206)))

  (it "returns partial file contents as body when header requests it"
    (-> (map->Request {:uri "/foo.png" :headers {"Range" "bytes=0-4"}})
        (response-should-have :body "/foo.png 0 5")))

  (it "returns content-type header based on file type"
    (->> (map->Request {:uri "/foo.png"})
         (run-get)
         ((func/flip get-in) [:headers "Content-Type"])
         (should= "image/png")))
)
