(ns http-server.request
  (:require [clojure.string :as str]))

(defrecord Request [method uri])

(defn parse [reader]
  (let [[method uri version] (str/split (.readLine reader) #" " 3)]
    (map->Request {:method method :uri uri :version version})))
