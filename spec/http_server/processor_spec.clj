(ns http-server.processor-spec
  (:require [speclj.core :refer :all]
            [http-server.processor :refer :all]
            [http-server.file-info :as fi]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (file-exists? [this path] (= path "/foo")))

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

  ;(it "returns file contents as body when file exists"
  ;  (-> (map->FakeFileInfo {:exists true})
  ;      (process-get "/foo")
  ;      (get :status)
  ;      (should= 200)))
)
