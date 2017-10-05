(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.router :refer :all]
            [http-server.file-info :as fi]
            [http-server.request])
  (:import [http_server.router DefaultGET Default]
           [http_server.request Request]))

(describe "route"
  (it "returns a DefaultGET for GET request"
    (->> (Request. "GET" "/whatever")
         (route)
         (should-be-a DefaultGET)))


  (it "returns a Default for other requests"
    (->> (Request. "POST" "/whatever")
         (route)
         (should-be-a Default)))
)

(defrecord FakeFileInfo []
  fi/FileInfo
  (file-exists? [this path] (= path "/foo"))
  (file-data [this path] "blah"))

(describe "process-get"

  (it "returns 404 when file does not exist"
    (-> (FakeFileInfo.)
        (process-get "/foobar")
        (get :status)
        (should= 404)))

  (it "returns 200 when file exists"
    (-> (FakeFileInfo.)
        (process-get "/foo")
        (get :status)
        (should= 200)))

  (it "returns file contents as body when file exists"
    (-> (FakeFileInfo.)
        (process-get "/foo")
        (get :body)
        (should= "blah")))
)
