(ns http-server.file-info)

(defprotocol FileInfo
  (file-exists? [this path]))
