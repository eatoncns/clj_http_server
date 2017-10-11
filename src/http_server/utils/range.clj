(ns http-server.utils.range
  (:require [http-server.utils.file-info :as fi]))

(def not-empty? (complement empty?))

(defn- full-range [range-start range-end]
  (let [start (Integer/parseInt range-start)
        end (Integer/parseInt range-end)]
    [start (+ (- end start) 1)]))

(defn- end-range [file-info path range-end]
  (let [file-length (fi/file-length file-info path)
        end (Integer/parseInt range-end)]
    [(- file-length end) end]))

(defn- start-range [file-info path range-start]
  (let [file-length (fi/file-length file-info path)
        start (Integer/parseInt range-start)]
    [start (- file-length start)]))

(defn calculate-range [file-info path range-string]
  (let [[_ range-start range-end] (re-matches #"bytes=(\d*)-(\d*)" range-string)]
    (cond
      (and (not-empty? range-start) (not-empty? range-end))
        (full-range range-start range-end)
      (empty? range-start)
        (end-range file-info path range-end)
      (empty? range-end)
        (start-range file-info path range-start))))
