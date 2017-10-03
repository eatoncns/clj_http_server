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
  (with-open [server-sock (ServerSocket. port)]
    (loop []
      (let [sock (.accept server-sock)
            msg-in (receive sock)
            msg-out (handler msg-in)]
        (respond sock msg-out)
        (.close sock)
        (recur)))))

(defn static-response [_]
  "HTTP/1.1 200 OK

  Hello World")

(defn start [port]
  (println (str "Serving at port " port))
  (serve port static-response))
