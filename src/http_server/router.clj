(ns http-server.router
  (:require [http-server.routes.default-get :refer [->DefaultGET]]
            [http-server.routes.directory :refer [->Directory]]
            [http-server.routes.file :refer [->File]]
            [http-server.routes.coffee :refer [->Coffee]]
            [http-server.routes.tea :refer [->Tea]]
            [http-server.routes.cookie :refer [->Cookie]]
            [http-server.routes.eat-cookie :refer [->EatCookie]]
            [http-server.routes.parameters :refer [->Parameters]]
            [http-server.routes.form :refer [->Form]]
            [http-server.routes.patch :refer [->Patch]]
            [http-server.routes.redirect :refer [->Redirect]]
            [http-server.routes.method-options :refer [->MethodOptions]]
            [http-server.routes.method-options2 :refer [->MethodOptions2]]
            [http-server.routes.logs :refer [->Logs]]
            [http-server.routes.not-authorised :refer [->NotAuthorised]]
            [http-server.routes.method-not-allowed :refer [->MethodNotAllowed]]
            [http-server.constants.methods :refer [http-methods]]
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
                         ->Patch
                         ->Logs
                         ->Directory
                         ->File
                         ->DefaultGET])

(defn- is-applicable [directory-served route]
  (route/is-applicable? route directory-served))


(defn- find-route [request directory-served]
  (let [routes (map #(%1 request) route-constructors)]
    (first (filter (partial is-applicable directory-served) routes))))


(defn- add-if-applicable [request directory-served allowed-methods method]
  (let [request-with-method (assoc request :method method)
        applicable-route (find-route request-with-method directory-served)]
    (if (nil? applicable-route)
      allowed-methods
      (conj allowed-methods method))))


(defn allowed-methods [request directory-served]
  (sort (reduce (partial add-if-applicable request directory-served) [] http-methods)))


(defn route [request directory-served]
  (let [applicable-route (find-route request directory-served)]
    (if (nil? applicable-route)
      (->MethodNotAllowed request (allowed-methods request directory-served))
      applicable-route)))
