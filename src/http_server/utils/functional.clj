(ns http-server.utils.functional)

(defn flip [f]
  (fn [& xs]
    (apply f (reverse xs))))
