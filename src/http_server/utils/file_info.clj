(ns http-server.utils.file-info
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import [java.io File FileInputStream]))

(defprotocol FileInfo
  (is-directory? [this path])
  (list-files [this path])
  (file-exists? [this path])
  (file-length [this path])
  (file-data [this path])
  (partial-file-data [this path start length])
  (patch-file [this path data]))

(defn- read-file [file start length]
  (let [array (byte-array length)
        input-stream (java.io.FileInputStream. file)]
      (.skip input-stream start)
      (.read input-stream array)
      (.close input-stream)
      array))

(defrecord FileInfoAtRoot [root]
  FileInfo
  (is-directory? [this path]
    (.isDirectory (io/as-file (str root path))))

  (list-files [this path]
    (.list (io/as-file (str root path))))

  (file-exists? [this path]
    (.isFile (io/as-file (str root path))))

  (file-length [this path]
    (.length (io/as-file (str root path))))

  (file-data [this path]
    (let [file (java.io.File. (str root path))
          length (.length file)]
      (read-file file 0 length)))

  (partial-file-data [this path start length]
    (let [file (java.io.File. (str root path))]
      (read-file file start length)))

  (patch-file [this path data]
    (spit (str root path) data))
)

(defn extension [path]
  (let [parts (string/split path #"\.")]
    (if (= (count parts) 1)
      "txt"
      (last parts))))
