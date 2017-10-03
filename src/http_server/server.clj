(ns http-server.server
  (:require [clojure.java.io :as io]
            [http-server.request :as req])
  (:import [java.net ServerSocket]))

(defn receive [socket]
  (.readLine (io/reader socket)))

(defn respond [socket, msg]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))

(defn serve [port, directory-served]
  (with-open [server-sock (ServerSocket. port)]
    (loop []
      (let [sock (.accept server-sock)
            raw-request (receive sock)
            request (req/parse raw-request)
            response (req/process request directory-served)]
        (respond sock response)
        (.close sock)
        (recur)))))

(defn start [port, directory-served]
  (println (str "Serving " directory-served " at port " port))
  (serve port directory-served))
