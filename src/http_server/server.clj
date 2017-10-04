(ns http-server.server
  (:require [clojure.java.io :as io]
            [http-server.request :as request]
            [http-server.processor :as processor]
            [http-server.response :as response])
  (:import [java.net ServerSocket]
           [java.util.concurrent Executors]))

(defn- read-request [socket]
  (.readLine (io/reader socket)))

(defn- send-response [msg, socket]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)
    (.close socket)))

(defn- handle-request [directory-served, socket]
  (-> socket
      (read-request)
      (request/parse)
      (processor/process directory-served)
      (response/build)
      (send-response socket)))

(defn- serve [port, directory-served]
  (let [thread-pool (Executors/newFixedThreadPool 20)
        request-handler (partial handle-request directory-served)]
    (with-open [server-sock (ServerSocket. port)]
      (loop []
        (let [sock (.accept server-sock)]
          (.execute thread-pool #(request-handler sock))
          (recur))))))

(defn start [port, directory-served]
  (println (str "Serving " directory-served " at port " port))
  (serve port directory-served))
