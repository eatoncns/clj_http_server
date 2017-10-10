(ns http-server.router-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.default-get :refer :all]
            [http-server.utils.file-info :as fi]
            [http-server.utils.functional :as func]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (is-directory? [this path] (= path "/dir"))
  (list-files [this path] ["hello.txt" "image.png" "file1"])
  (file-exists? [this path] (= path "/foo.png"))
  (file-data [this path] "blah"))

(defn run-get [path]
  (->> (FakeFileInfo.)
       (process-get path)))

(describe "process-get"

  (it "returns 200 when path is a valid directory"
    (->> (run-get "/dir")
         (:status)
         (should= 200)))

  (it "returns file listing page as body for valid directory"
    (->> (run-get "/dir")
         (:body)
         (should-contain "hello.txt")))

  (it "returns 404 when path does not exist"
    (->> (run-get "/foobar")
         (:status)
         (should= 404)))

  (it "returns 200 when path is a file that exists"
    (->> (run-get "/foo.png")
         (:status)
         (should= 200)))

  (it "returns file contents as body when path is a file that exists"
    (->> (run-get "/foo.png")
         (:body)
         (should= "blah")))

  (it "returns content-type header based on file type"
    (->> (run-get "/foo.png")
         ((func/flip get-in) [:headers "Content-Type"])
         (should= "image/png")))
)
