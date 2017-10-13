(ns http-server.logger
  (:require [clojure.java.io :as io]
            [clj-logging-config.log4j :as log-config]
            [clojure.tools.logging :as logging]))

(def log-file-name "/tmp/server-log.txt")
(io/delete-file log-file-name :quiet)
(log-config/set-logger! :out (org.apache.log4j.FileAppender.
                             (org.apache.log4j.EnhancedPatternLayout. org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
                             log-file-name
                             true))

(defn log [msg]
  (logging/info msg))

(defn log-request [request]
  (logging/info (str (:method request) " "
             (:uri request) " "
             (:version request) " "
             (:params request) " "
             (:headers request) " "
             (:body request)))
  request)

(defn log-response [response]
  (logging/info (str (:status response) " "
             (:headers response) " "
             (:body response)))
  response)
