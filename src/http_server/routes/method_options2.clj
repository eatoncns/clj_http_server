(ns http-server.routes.method-options2
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.utils.functional :refer [in?]]))

(defrecord MethodOptions2 [request]
  route/Route
  (is-applicable? [this directory-served]
    (let [uri (get-in this [:request :uri])
          method (get-in this [:request :method])]
      (and (= uri "/method_options2")
           (in? method ["GET" "OPTIONS"]))))

  (process [this directory-served]
    (map->Response {:status 200
                    :headers {"Allow" "GET,OPTIONS"}
                    :body nil}))
)
