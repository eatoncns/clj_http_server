(ns http-server.core
  (:require [http-server.server :as server]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [
   ["-p" "--port PORT" "Port number"
    :default 5000
    :parse-fn #(Integer/parseInt %)]
   ["-d" "--directory DIR" "Serve directory"
    :default (System/getenv "PUBLIC_DIR")]
   ])

(defn get-opts
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    options))

(defn -main
  [& args]
  (let [options (get-opts args)]
    (server/start (:port options) (:directory options))))
