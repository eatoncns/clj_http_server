(ns http-server.response
  (:import [java.io ByteArrayOutputStream]))

(defrecord Response [status body])

(def reasons { 200 "OK"
               404 "Not Found"})

(def crlf "\r\n")

(defn- build-status-line [status]
 (str "HTTP/1.1 " status " " (get reasons status) crlf))

(defprotocol WriteBytes
  (write-bytes [x writer]))

(extend-protocol WriteBytes
  (Class/forName "[B")
  (write-bytes [x writer]
    (.write writer x 0 (alength x))
     writer)

  nil
  (write-bytes [x, writer] writer)

  java.lang.String
  (write-bytes [x writer] (write-bytes (.getBytes x) writer)))

(defn build
  [response]
  (let [{:keys [status body]} response]
    (->> (ByteArrayOutputStream.)
         (write-bytes (build-status-line status))
         (write-bytes crlf)
         (write-bytes body)
         (.toByteArray))))
