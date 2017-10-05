(ns http-server.response-spec
  (:require [speclj.core :refer :all]
            [http-server.response :refer :all]))

(describe "build"

  (it "builds response beginning with status line"
    (->> (map->Response {:status 200 :body "stuff"})
         (build)
         (String.)
         (should-contain #"^HTTP/1\.1 200 OK\r\n")))

  (it "builds response ending with body"
    (->> (map->Response {:status 200 :body "stuff"})
         (build)
         (String.)
         (should-contain #"stuff$")))
)
