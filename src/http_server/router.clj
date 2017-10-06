(ns http-server.router
  (:require [http-server.routes.default-get :refer [->DefaultGET]]
            [http-server.routes.default :refer [->Default]]
            [http-server.routes.coffee :refer [->Coffee]]
            [http-server.routes.tea :refer [->Tea]]
            [http-server.routes.redirect :refer [->Redirect]]
            [http-server.routes.method-options :refer [->MethodOptions]]
            [http-server.routes.route :as route]))

(def route-constructors [->MethodOptions ->Redirect ->Tea ->Coffee ->DefaultGET ->Default])

(defn route [request]
  (let [routes (map #(%1 request) route-constructors)]
    (first (filter route/is-applicable routes))))
