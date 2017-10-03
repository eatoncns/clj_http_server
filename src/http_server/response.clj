(ns http-server.response)

(defrecord Response [status body])

(def reasons { 200 "OK" })

(defn build
  [response]
  (let [{:keys [status body]} response]
    (str "HTTP/1.1 " status " " (get reasons status) "\r\n\r\n" body)))
