(ns http-server.utils.file-info
  (:require [clojure.java.io :as io])
  (:import [java.io File FileInputStream]))

(defprotocol FileInfo
  (file-exists? [this path])
  (file-data [this path]))

(defrecord FileInfoAtRoot [root]
  FileInfo
  (file-exists? [this path]
    (.isFile (io/as-file (str root path))))

  (file-data [this path]
    (let [file (java.io.File. (str root path))
          array (byte-array (.length file))
          input-stream (java.io.FileInputStream. file)]
      (.read input-stream array)
      (.close input-stream)
      array))
)
