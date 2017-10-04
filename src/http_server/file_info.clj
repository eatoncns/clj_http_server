(ns http-server.file-info
  (:require [clojure.java.io :as io]))

(defprotocol FileInfo
  (file-exists? [this path])
  (file-data [this path]))

(defrecord FileInfoAtRoot [root]
  FileInfo
  (file-exists? [this path]
      (.exists (io/file (str root path))))

  (file-data [this path]
    (slurp (str root path))))
