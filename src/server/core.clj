(ns server.core
  (:require [clojure.java.io :as io])
  (:import [java.net ServerSocket])
  (:gen-class))

(defn receive [socket]
  (.readLine (io/reader socket)))

(defn send [socket, msg]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))

(defn serve [port, handler]
  (with-open [server-sock (ServerSocket. port)
              sock (.accept server-sock)]
    (let [msg-in (receive sock)
          msg-out (handler msg-in)]
      (send sock msg-out))))

(defn static-response [_]
  "HTTP/1.1 200 OK

  Hello World")

(defn -main
  [& args]
  (serve 5000 static-response))
