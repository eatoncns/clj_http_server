(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.default-get :refer :all]
            [http-server.file-info :as fi]))

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
