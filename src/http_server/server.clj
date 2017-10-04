(ns http-server.server
  (:require [clojure.java.io :as io]
            [http-server.request :as request]
            [http-server.processor :as processor]
            [http-server.response :as response])
  (:import [java.net ServerSocket]))

(defn read-request [socket]
  (.readLine (io/reader socket)))

(defn send-response [msg, socket]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)
    (.close socket)))

(defn serve [port, directory-served]
  (with-open [server-sock (ServerSocket. port)]
    (loop []
      (let [sock (.accept server-sock)]
        (-> sock
            (read-request)
            (request/parse)
            (processor/process directory-served)
            (response/build)
            (send-response sock))
        (recur)))))

(defn start [port, directory-served]
  (println (str "Serving " directory-served " at port " port))
  (serve port directory-served))
