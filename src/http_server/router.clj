(ns http-server.router
  (:require [http-server.routes.default-get :refer [->DefaultGET]]
            [http-server.routes.default :refer [->Default]]
            [http-server.routes.coffee :refer [->Coffee]]
            [http-server.routes.tea :refer [->Tea]]
            [http-server.routes.cookie :refer [->Cookie]]
            [http-server.routes.eat-cookie :refer [->EatCookie]]
            [http-server.routes.parameters :refer [->Parameters]]
            [http-server.routes.form :refer [->Form]]
            [http-server.routes.redirect :refer [->Redirect]]
            [http-server.routes.method-options :refer [->MethodOptions]]
            [http-server.routes.method-options2 :refer [->MethodOptions2]]
            [http-server.routes.logs :refer [->Logs]]
            [http-server.routes.not-authorised :refer [->NotAuthorised]]
            [http-server.routes.route :as route]))

(def route-constructors [->NotAuthorised
                         ->MethodOptions
                         ->MethodOptions2
                         ->Redirect
                         ->Tea
                         ->Coffee
                         ->Cookie
                         ->EatCookie
                         ->Parameters
                         ->Form
                         ->Logs
                         ->DefaultGET
                         ->Default])

(defn route [request]
  (let [routes (map #(%1 request) route-constructors)]
    (first (filter route/is-applicable routes))))
