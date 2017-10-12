(ns http-server.directory-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.directory :refer :all]
            [http-server.utils.file-info :as fi]
            [http-server.request :refer [map->Request]]
            [http-server.constants.methods :refer :all]
            [http-server.spec-helper :refer [rshould=]]
            [clojure.set :as s]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (is-directory? [this path] (= path "/dir"))
  (list-files [this path] ["hello.txt" "image.png" "file1"]))

(defn run-applicable [request]
  (is-request-applicable request (FakeFileInfo.)))

(defn applicable-should= [request expected]
  (->> request
       (run-applicable)
       (should= expected)))

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

(defn request [method uri]
  (map->Request {:method method :uri uri}))

(describe "is-request-applicable"

  (it "returns true for GET to directory"
    (-> (request GET "/dir")
        (applicable-should= true)))

  (it "returns true for HEAD to directory"
    (-> (request HEAD "/dir")
        (applicable-should= true)))

  (it "returns false for GET to non-directory"
    (-> (request GET "/whatever")
        (applicable-should= false)))

  (it "returns false for HEAD to non-directory"
    (-> (request HEAD "/whatever")
        (applicable-should= false)))

  (it "returns false for other methods"
    (doseq [method (s/difference http-methods #{GET HEAD})]
      (-> (request method "/dir")
          (applicable-should= false))))
)

(describe "process-request for GET"
  (it "returns 200 when path is a valid directory"
    (-> (request GET "/dir")
        (response-should-have :status 200)))

  (it "returns file listing page as body for valid directory"
    (->> (request GET "/dir")
         (run-request)
         (:body)
         (should-contain "hello.txt")))

  (it "returns html content type header"
    (-> (request GET "/dir")
        (response-should-have-header "Content-Type" "text/html")))
)

(describe "process-request for HEAD"
  (it "returns 200 when path is a valid directory"
    (-> (request HEAD "/dir")
        (response-should-have :status 200)))

  (it "returns no body"
    (-> (request HEAD "/dir")
        (response-should-have :body nil)))

  (it "returns html content type header"
    (-> (request HEAD "/dir")
        (response-should-have-header "Content-Type" "text/html")))
)
