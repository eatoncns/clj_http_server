(ns http-server.request-spec
  (:require [speclj.core :refer :all]
            [http-server.request :refer :all]
            [clojure.java.io :as io]))

(defn as-reader [request]
  (io/reader (char-array request)))

(defn run-parse [request-string]
  (->> request-string
       (as-reader)
       (parse)))

(describe "parse"

  (it "parses method"
    (->> "GET / HTTP/1.1\r\n\r\n"
         (run-parse)
         (:method)
         (should= "GET")))

  (it "parses URI"
    (->> "GET /foo HTTP/1.1\r\n\r\n"
         (run-parse)
         (:uri)
         (should= "/foo")))

  (it "parses single param"
    (->> "GET /foo?type=chocolate HTTP/1.1\r\n\r\n"
         (run-parse)
         (:params)
         (should== {"type" "chocolate"})))

  (it "parses multiple params"
    (->> "GET /foo?type=chocolate&blah=stuff HTTP/1.1\r\n\r\n"
         (run-parse)
         (:params)
         (should== {"type" "chocolate", "blah" "stuff"})))

  (it "parses encoded params"
    (->> "GET /foo?var=Blah%20%3C%2C HTTP/1.1\r\n\r\n"
         (run-parse)
         (:params)
         (should== {"var" "Blah <,"})))

  (it "parses version"
    (->> "GET /foo HTTP/1.1\r\n\r\n"
         (run-parse)
         (:version)
         (should= "HTTP/1.1")))

  (it "parses headers"
    (->> "GET /foo HTTP/1.1\r\nHost: localhost\r\nConnection: Keep-Alive\r\n\r\n"
         (run-parse)
         (:headers)
         (should== {"Connection" "Keep-Alive" "Host" "localhost"})))
)
