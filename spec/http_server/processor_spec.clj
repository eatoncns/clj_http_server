(ns http-server.processor-spec
  (:require [speclj.core :refer :all]
            [http-server.processor :refer :all]
            [http-server.file-info :as fi]))

(defrecord FakeFileInfo [exists]
  fi/FileInfo
  (file-exists? [this path] (:exists this)))

(describe "process-get"

  (it "returns 404 when file does not exist"
    (-> (map->FakeFileInfo {:exists false})
        (process-get "/foo")
        (get :status)
        (should= 404)))

  (it "returns 200 when file does not exist"
    (-> (map->FakeFileInfo {:exists true})
        (process-get "/foo")
        (get :status)
        (should= 200)))
)
