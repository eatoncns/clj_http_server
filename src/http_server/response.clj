(ns http-server.response
  (:import [java.io ByteArrayOutputStream]))

(defrecord Response [status body])

; TODO move somewhere more appropriate
(defn b-concat [lhs rhs]
  (let [lhs-len (alength lhs)
       rhs-len (alength rhs)
       writer (ByteArrayOutputStream. (+ lhs-len rhs-len))]
    (.write writer lhs 0 lhs-len)
    (.write writer rhs 0 rhs-len)
    (.toByteArray writer)))

(def reasons { 200 "OK"
               404 "Not Found"})

(defn build-status-line [status]
 (str "HTTP/1.1 " status " " (get reasons status) "\r\n"))

(defn build
  [response]
  (let [{:keys [status body]} response
        status-line (build-status-line status)
        crlf-line "\r\n"
        non-body (str status-line crlf-line)]
    (b-concat (.getBytes non-body) body)))
