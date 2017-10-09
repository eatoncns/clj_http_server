(ns http-server.auth-spec
  (:require [speclj.core :refer :all]
            [http-server.auth :refer :all]
            [http-server.request :refer [map->Request]]
            [clojure.data.codec.base64 :as b64]))

(defn encode [in]
  (-> in
      (.getBytes)
      (b64/encode)
      (String.)))

(def auth-config {"/logs" {:username "admin" :password "hunter2"}})
(def matching-credentials (encode "admin:hunter2"))

(describe authorise

  (it "sets authorised true when uri not in config"
    (-> (map->Request{:method "GET" :uri "/foo" :version "HTTP/1.1" :headers {}})
        (authorise auth-config)
        (get :authorised)
        (should= true)))

  (it "sets authorised false for matching uri with no header"
    (-> (map->Request{:method "GET" :uri "/logs" :version "HTTP/1.1" :headers {}})
        (authorise auth-config)
        (get :authorised)
        (should= false)))

  (it "sets authorised true for matching uri with correct header"
    (-> (map->Request{:method "GET"
                      :uri "/logs"
                      :version "HTTP/1.1"
                      :headers {"Authorization" (str "Basic " matching-credentials)}})
        (authorise auth-config)
        (get :authorised)
        (should= true)))

  (it "sets authorised false for matching uri with invalid syntax header"
    (-> (map->Request{:method "GET"
                      :uri "/logs"
                      :version "HTTP/1.1"
                      :headers {"Authorization" (str "Bas" matching-credentials)}})
        (authorise auth-config)
        (get :authorised)
        (should= false)))

  (it "sets authorised false for matching uri with incorrect credentials"
    (-> (map->Request{:method "GET"
                      :uri "/logs"
                      :version "HTTP/1.1"
                      :headers {"Authorization" (str "Basic " (encode "admin:hunter3"))}})
        (authorise auth-config)
        (get :authorised)
        (should= false)))
)
