(ns http-server.constants.reasons)

(def reasons { 200 "OK"
               206 "Partial Content"
               401 "Unauthorized"
               404 "Not Found"
               405 "Method Not Allowed"
               412 "Precondition Failed"
               418 "I'm a teapot"
               422 "Unprocessable Entity"})
