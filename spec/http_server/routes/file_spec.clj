(ns http-server.routes.file-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.file :refer :all]
            [http-server.utils.file-info :as fi]
            [http-server.spec-helper :refer [rshould=]]
            [http-server.request :refer [map->Request]]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (file-exists? [this path] (= path "/foo.png"))
  (file-length [this path] 100)
  (file-data [this path] "blah")
  (partial-file-data [this path start length] (str path " " start " " length)))

(defn applicable-should= [request expected]
  (-> request
      (is-request-applicable (FakeFileInfo.))
      (rshould= expected)))

(defn run-request [request]
  (process-request request (FakeFileInfo.)))

(defn response-should-have [request field expected]
  (->> request
       (run-request)
       (field)
       (should= expected)))

(defn response-should-have-header [request header expected]
  (-> request
      (run-request)
      (get-in [:headers header])
      (rshould= expected)))

(defn request
  ([method uri]
    (request method uri {}))
  ([method uri headers]
    (map->Request {:method method :uri uri :headers headers})))

(describe "is-request-applicable"
  (it "returns true for GET on file that exists"
    (applicable-should= (request GET "/foo.png") true))

  (it "returns true for HEAD on file that exists"
    (applicable-should= (request HEAD "/foo.png") true))

  (it "returns false for GET on uri that doesn't correspond to an existing file"
    (applicable-should= (request GET "/food.png") false))

  (it "returns false for HEAD on uri that doesn't correspond to an existing file"
    (applicable-should= (request HEAD "/food.png") false))

  (it "returns false for other methods"
    (doseq [method (s/difference http-methods #{GET HEAD})]
      (applicable-should= (request method "/foo.png") false)))
)

(describe "process-request for GET"
  (it "returns 200 when path is a file that exists"
    (-> (request GET "/foo.png")
        (response-should-have :status 200)))

  (it "returns file contents as body"
    (-> (request GET "/foo.png")
        (response-should-have :body "blah")))

  (it "returns content-type header based on file type"
    (-> (request GET "/foo.png")
        (response-should-have-header "Content-Type" "image/png")))

  (it "returns 206 for partial file content"
    (-> (request GET "/foo.png" {"Range" "bytes=0-4"})
        (response-should-have :status 206)))

  (it "returns partial file contents as body when header requests it"
    (-> (request GET "/foo.png" {"Range" "bytes=0-4"})
        (response-should-have :body "/foo.png 0 5")))
)
