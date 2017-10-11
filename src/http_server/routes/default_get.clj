(ns http-server.routes.default-get
  (:require [http-server.response :refer [map->Response content-type]]
            [http-server.utils.file-info :as fi]
            [http-server.utils.html :as html]
            [http-server.routes.route :as route]
            [clojure.string :as string])
  (:import [http_server.response Response]
           [http_server.utils.file_info FileInfoAtRoot]))

(defn requests-range? [request]
  (let [headers (get request :headers)]
    (contains? headers "Range")))

(def not-empty? (complement empty?))

(defn full-range [range-start range-end]
  (let [start (Integer/parseInt range-start)
        end (Integer/parseInt range-end)]
    [start (+ (- end start) 1)]))

(defn end-range [file-info path range-end]
  (let [file-length (fi/file-length file-info path)
        end (Integer/parseInt range-end)]
    [(- file-length end) end]))

(defn start-range [file-info path range-start]
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

(defn partial-file-data [file-info path request]
  (let [range-string (get-in request [:headers "Range"])
        [start length] (calculate-range file-info path range-string)]
    (fi/partial-file-data file-info path start length)))

(defn process-get [request, file-info]
  (let [path (get request :uri)]
    (cond
      (fi/is-directory? file-info path)
        (map->Response {:status 200
                        :headers (content-type "html")
                        :body (html/list-of-links (fi/list-files file-info path))})
      (and (fi/file-exists? file-info path) (requests-range? request))
        (map->Response {:status 206
                        :headers (content-type (fi/extension path))
                        :body (partial-file-data file-info path request)})
      (fi/file-exists? file-info path)
        (map->Response {:status 200
                        :headers (content-type (fi/extension path))
                        :body (fi/file-data file-info path)})
      :else
        (map->Response {:status 404
                        :headers {}
                        :body nil}))))

(defrecord DefaultGET [request]
  route/Route
  (is-applicable [this]
    (= (get-in this [:request :method]) "GET"))

  (process [this directory-served]
    (process-get (get this :request) (FileInfoAtRoot. directory-served))))
