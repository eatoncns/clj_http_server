(ns http-server.utils.functional
  (:require [clojure.string :as string]))

(defn flip [f]
  (fn [& xs]
    (apply f (reverse xs))))

(defn split-map
  ([coll line divider]
   (split-map coll line divider string/trim))
  ([coll line divider formatter]
    (let [[k v] (string/split line divider 2)]
      (assoc coll (formatter k) (formatter v)))))
