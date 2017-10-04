(ns http-server.request-spec
  (:require [speclj.core :refer :all]
            [http-server.request :refer :all]))


(describe "parse"

  (it "parses GET method"
    (let [request (parse "GET / HTTP/1.1\r\n\r\n")]
      (should= "GET" (:method request))))

  (it "parses POST method"
    (let [request (parse "POST / HTTP/1.1\r\n\r\n")]
      (should= "POST" (:method request))))

  (it "parses URI"
    (let [request (parse "GET /foo HTTP/1.1\r\n\r\n")]
      (should= "/foo" (:uri request))))
)
