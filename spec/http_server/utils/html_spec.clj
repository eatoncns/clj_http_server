(ns http-server.utils.html-spec
  (:require [speclj.core :refer :all]
            [http-server.utils.html :refer :all]))

(describe "list-of-links"

  (it "returns a list of links to input filenames"
      (->> ["file1" "image.png" "joe.txt"]
           (list-of-links)
           (should= (str "<ul>"
                         "<li><a href=\"/file1\">file1</a></li>"
                         "<li><a href=\"/image.png\">image.png</a></li>"
                         "<li><a href=\"/joe.txt\">joe.txt</a></li>"
                         "</ul>"))))
)
