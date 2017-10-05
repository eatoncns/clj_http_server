(ns http-server.utils.bytes)

(defprotocol WriteBytes
  (write [x writer]))

(extend-protocol WriteBytes
  (Class/forName "[B")
  (write [x writer]
    (.write writer x 0 (alength x))
     writer)

  nil
  (write [x, writer] writer)

  java.lang.String
  (write [x writer] (write (.getBytes x) writer)))
