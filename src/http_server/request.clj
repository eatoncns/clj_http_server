(ns http-server.request)

(defrecord Request [method uri])

(defn parse [input]
  (map->Request {:method "GET" :uri "/foo"}))

(defn process [request]
  "HTTP/1.1 200 OK\r\n\r\nHello World")
