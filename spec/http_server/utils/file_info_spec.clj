(ns http-server.utils.file-info-spec
  (:require [speclj.core :refer :all]
            [http-server.utils.file-info :refer :all]))

(describe "extension"

  (it "returns extension from simple path"
    (->> "/foo.png"
         (extension)
         (should= "png")))

  (it "defaults to txt when no extension present"
    (->> "/foo"
         (extension)
         (should= "txt")))
)
