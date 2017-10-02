(ns http-server.core
  (:require [http-server.server :as server])
  (:gen-class))

(defn -main
  [& args]
  (server/start))
