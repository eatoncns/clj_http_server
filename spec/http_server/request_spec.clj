(ns http-server.request-spec
  (:require [speclj.core :refer :all]
            [http-server.request :refer :all]
            [clojure.java.io :as io]))

(defn as-reader [request]
  (io/reader (char-array request)))

(describe "parse"

  (it "parses method"
    (->> "GET / HTTP/1.1\r\n\r\n"
         (as-reader)
         (parse)
         (:method)
         (should= "GET")))

  (it "parses URI"
    (->> "GET /foo HTTP/1.1\r\n\r\n"
         (as-reader)
         (parse)
         (:uri)
         (should= "/foo")))

  (it "parses version"
    (->> "GET /foo HTTP/1.1\r\n\r\n"
         (as-reader)
         (parse)
         (:version)
         (should= "HTTP/1.1")))

  (it "parses headers"
    (->> "GET /foo HTTP/1.1\r\nHost: localhost\r\nConnection: Keep-Alive\r\n\r\n"
         (as-reader)
         (parse)
         (:headers)
         (should== {"Connection" "Keep-Alive" "Host" "localhost"})))
)
