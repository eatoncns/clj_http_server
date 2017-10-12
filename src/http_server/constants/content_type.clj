(ns http-server.constants.content-type)

(def content-types {"html" "text/html"
                    "txt" "text/plain"
                    "png" "image/png"
                    "jpeg" "image/jpeg"
                    "gif" "image/gif"})

(defn content-type [shorthand]
  {"Content-Type" (get content-types shorthand)})
