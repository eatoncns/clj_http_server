(ns http-server.logger
  (:require [taoensso.timbre :refer [info]]))

(defn log [msg]
  (info msg)
  msg)
