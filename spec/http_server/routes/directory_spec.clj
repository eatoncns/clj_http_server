(ns http-server.directory-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.directory :refer :all]
            [http-server.utils.file-info :as fi]
            [http-server.request :refer [map->Request]]
            [http-server.constants.methods :refer :all]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (is-directory? [this path] (= path "/dir"))
  (list-files [this path] ["hello.txt" "image.png" "file1"]))

(defn run-applicable [request]
  (is-request-applicable request (FakeFileInfo.)))

(describe "is-request-applicable"

  (it "returns true for GET to directory"
    (->> (map->Request {:uri "/dir" :method GET})
         (run-applicable)
         (should= true)))

  (it "returns true for HEAD to directory"
    (->> (map->Request {:uri "/dir" :method GET})
         (run-applicable)
         (should= true)))

  (it "returns false for GET to non-directory"
    (->> (map->Request {:uri "/whatever" :method GET})
         (run-applicable)
         (should= false)))

  (it "returns false for HEAD to non-directory"
    (->> (map->Request {:uri "/whatever" :method HEAD})
         (run-applicable)
         (should= false)))

  ;(it "returns false for other methods"
  ;  (doseq [method (set/difference http-methods #{GET HEAD})]
  ;  )
)
