(ns http-server.utils.range-spec
  (:require [speclj.core :refer :all]
            [http-server.utils.range :refer :all]
            [http-server.utils.file-info :as fi]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (file-length [this path] 100))

(describe "calculate-range"

  (it "calculates start and length from a full range"
    (->> "bytes=0-4"
         (calculate-range (FakeFileInfo.) "/whatever")
         (should= [0 5])))

  (it "calculates start and length from an end range"
    (->> "bytes=-4"
         (calculate-range (FakeFileInfo.) "/whatever")
         (should= [96 4])))

  (it "calculates start and length from a start range"
    (->> "bytes=4-"
         (calculate-range (FakeFileInfo.) "/whatever")
         (should= [4 96])))
)
