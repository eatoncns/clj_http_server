(ns http-server.routes.cookie
  (:require [http-server.routes.route :as route]
            [http-server.response :refer [map->Response]]
            [http-server.constants.methods :refer [GET]]
            [http-server.constants.content-type :refer [content-type]]))


(defn- build-cookie [m k v]
  (str m k "=" v))

(defrecord Cookie [request]
  route/Route
  (is-applicable? [this directory-served]
    (and (= (get-in this [:request :uri]) "/cookie")
         (= (get-in this [:request :method]) GET)))

  (process [this directory-served]
    (let [cookie (reduce-kv build-cookie "" (get-in this [:request :params]))]
      (map->Response {:status 200
                      :headers (merge {"Set-Cookie" cookie} (content-type "txt"))
                      :body "Eat"})))
)

