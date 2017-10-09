(ns http-server.utils.base64
  (:require [clojure.data.codec.base64 :as b64]))

(defn decode-string [in]
  (-> in
      (.getBytes)
      (b64/decode)
      (String.)))

(defn encode-string [in]
  (-> in
      (.getBytes)
      (b64/encode)
      (String.)))
