(ns http-server.response-spec
  (:require [speclj.core :refer :all]
            [http-server.response :refer :all]))

(describe "build"

 (it "builds response beginning with status line"
   (let [response (map->Response {:status 200 :body "Whatever"})
         raw-response (build response)]
     (should-contain #"^HTTP/1\.1 200 OK.*" raw-response)))

 (it "builds response ending with body"
   (let [response (map->Response {:status 200 :body "Whatever"})
         raw-response (build response)]
     (should-contain #".*\r\n\r\nWhatever$" raw-response)))
)
