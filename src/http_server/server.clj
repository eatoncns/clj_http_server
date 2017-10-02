(ns http-server.server
  (:require [clojure.java.io :as io])
  (:import [java.net ServerSocket]))

(defn receive [socket]
  (.readLine (io/reader socket)))

(defn respond [socket, msg]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))

(defn serve [port, handler]
  (with-open [server-sock (ServerSocket. port)
              sock (.accept server-sock)]
    (let [msg-in (receive sock)
          msg-out (handler msg-in)]
      (respond sock msg-out))))

(defn static-response [_]
  "HTTP/1.1 200 OK

  Hello World")

(defn start []
  (serve 5000 static-response))
