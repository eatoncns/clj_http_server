(ns http-server.response-spec
  (:require [speclj.core :refer :all]
            [http-server.response :refer :all]))

(describe "build-status-line"

  (it "builds line from status"
    (->> 200
       (build-status-line)
       (should-contain #"^HTTP/1\.1 200 OK\r\n")))
)
