(ns http-server.utils.bytes)

(defprotocol Bytes
  (write [x writer])
  (length [x]))

(extend-protocol Bytes
  (Class/forName "[B")
  (write [x writer]
    (.write writer x 0 (alength x))
     writer)
  (length [x] (alength x))

  nil
  (write [x, writer] writer)
  (length [x] 0)

  java.lang.String
  (write [x writer] (write (.getBytes x) writer))
  (length [x] (length (.getBytes x))))
