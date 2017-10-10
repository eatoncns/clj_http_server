(ns http-server.utils.file-info
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import [java.io File FileInputStream]))

(defprotocol FileInfo
  (is-directory? [this path])
  (list-files [this path])
  (file-exists? [this path])
  (file-data [this path]))

(defrecord FileInfoAtRoot [root]
  FileInfo
  (is-directory? [this path]
    (.isDirectory (io/as-file (str root path))))

  (list-files [this path]
    (.list (io/as-file (str root path))))

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

(defn extension [path]
  (let [parts (string/split path #"\.")]
    (if (= (count parts) 1)
      "txt"
      (last parts))))
