(ns http-server.auth
  (:require [http-server.utils.base64 :as b64]
            [http-server.utils.functional :as func]
            [clojure.string :as string]))

(defn- set-authorised [request auth-result]
  (assoc request :authorised auth-result))

(def auth-key "Authorization")

(defn- compare-credentials [request credentials uri-config]
  (if (= credentials uri-config)
    (set-authorised request true)
    (set-authorised request false)))

(defn- decode [encoded-credentials]
  (-> encoded-credentials
      (b64/decode-string)
      (string/split #":" 2)
      ((func/flip zipmap) [:username :password])))

(defn- check-credentials [request uri-config]
  (let [header-string (get-in request [:headers auth-key])
        [_ encoded-credentials] (re-matches #"Basic (.*)" header-string)]
    (if encoded-credentials
         (compare-credentials request (decode encoded-credentials) uri-config)
         (set-authorised request false))))

(defn- check-authorisation [request uri-config]
  (if (contains? (:headers request) auth-key)
    (check-credentials request uri-config)
    (set-authorised request false)))

(defn authorise [request auth-config]
  (if (contains? auth-config (:uri request))
    (check-authorisation request (get auth-config (:uri request)))
    (set-authorised request true)))
