(ns http-server.response
  (:import [java.io ByteArrayOutputStream]))

(defrecord Response [status body])

(def reasons { 200 "OK"
               404 "Not Found"})
(def crlf "\r\n")

(defn build-status-line [status]
 (str "HTTP/1.1 " status " " (get reasons status) crlf))

(defn write [writer line]
  (if (some? line)
    (.write writer line 0 (alength line))))

(defn write-string [writer input]
  (write writer (.getBytes input)))

(defn build
  [response]
  (let [{:keys [status body]} response
        writer (ByteArrayOutputStream.)]
    (write-string writer (build-status-line status))
    (write-string writer crlf)
    (write writer body)
    (.toByteArray writer)))
