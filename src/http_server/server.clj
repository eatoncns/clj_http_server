(ns http-server.server
  (:require [clojure.java.io :as io]
            [http-server.request :as req]
            [http-server.processor :as proc]
            [http-server.response :as res])
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
            response (proc/process request directory-served)
            raw-response (res/build response)]
        (respond sock raw-response)
        (.close sock)
        (recur)))))

(defn start [port, directory-served]
  (println (str "Serving " directory-served " at port " port))
  (serve port directory-served))
