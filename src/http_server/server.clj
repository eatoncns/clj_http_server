(ns http-server.server
  (:require [clojure.java.io :as io]
            [http-server.request :as request]
            [http-server.router :as router]
            [http-server.routes.route :as route]
            [http-server.response :as response])
  (:import [java.net ServerSocket]
           [java.util.concurrent Executors]))

(defn- read-request [socket]
  (.readLine (io/reader socket)))

(defn- send-response [msg, socket]
  (with-open [writer (io/output-stream socket)]
    (.write writer msg)))

(defn- handle-request [directory-served, socket]
  (try
    (-> socket
        (read-request)
        (request/parse)
        (router/route)
        (route/process directory-served)
        (response/build)
        (send-response socket))
    (finally (.close socket))))

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
