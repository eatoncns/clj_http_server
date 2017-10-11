(ns http-server.logger
  (:require [clojure.java.io :as io]
            [taoensso.timbre :as timbre :refer [info]]
            [taoensso.timbre.appenders.core :as appenders]))

(def log-file-name "/tmp/server-log.txt")
(io/delete-file log-file-name :quiet)
(timbre/merge-config! {:appenders {:spit (appenders/spit-appender {:fname log-file-name})}})

(defn log [msg]
  (info msg))

(defn log-request [request]
  (info (str (:method request) " "
             (:uri request) " "
             (:version request) " "
             (:params request) " "
             (:headers request) " "
             (:body request)))
  request)

(defn log-response [response]
  (info (str (:status response) " "
             (:headers response) " "
             (:body response)))
  response)
