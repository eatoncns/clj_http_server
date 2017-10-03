(ns http-server.response-spec
  (:require [speclj.core :refer :all]
            [http-server.response :refer :all]))

(describe "build"

  (it "builds response beginning with status line"
    (->> (map->Response {:status 200 :body "Whatever"})
         (build)
         (should-contain #"^HTTP/1\.1 200 OK.*")))

  (it "builds response ending with body"
    (->> (map->Response {:status 200 :body "Whatever"})
         (build)
         (should-contain #".*\r\n\r\nWhatever$")))
)
