(ns http-server.server
  (:require [clojure.java.io :as io]
            [http-server.request :as request]
            [http-server.logger :as logger]
            [http-server.auth :as auth]
            [http-server.router :as router]
            [http-server.routes.route :as route]
            [http-server.response :as response])
  (:import [java.net ServerSocket]
           [java.util.concurrent Executors]))

(defn- send-response [msg, socket]
  (with-open [writer (io/output-stream socket)]
    (.write writer msg)))

(defn- handle-request [directory-served, auth-config, socket]
  (try
    (with-open [reader (io/reader socket)]
      (-> reader
          (request/parse)
          (logger/log-request)
          (auth/authorise auth-config)
          (router/route)
          (route/process directory-served)
          (logger/log-response)
          (response/build)
          (send-response socket)))
    (finally (.close socket))))

(defn- serve [port, directory-served, auth-config]
  (let [thread-pool (Executors/newFixedThreadPool 20)
        request-handler (partial handle-request directory-served auth-config)]
    (with-open [server-sock (ServerSocket. port)]
      (loop []
        (let [sock (.accept server-sock)]
          (.execute thread-pool #(request-handler sock))
          (recur))))))

(defn start [port, directory-served auth-config]
  (logger/log (str "Serving " directory-served " at port " port))
  (serve port directory-served auth-config))
