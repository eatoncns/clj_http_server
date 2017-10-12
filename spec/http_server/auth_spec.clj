(ns http-server.auth-spec
  (:require [speclj.core :refer :all]
            [http-server.auth :refer :all]
            [http-server.request :refer [map->Request]]
            [http-server.utils.base64 :as b64]
            [http-server.spec-helper :refer [rshould=]]))

(def auth-config {"/logs" {:username "admin" :password "hunter2"}})
(def matching-credentials (b64/encode-string "admin:hunter2"))

(defn authorised-should= [request expected]
  (-> request
      (authorise auth-config)
      (get :authorised)
      (rshould= expected)))

(describe authorise

  (it "sets authorised true when uri not in config"
    (-> (map->Request{:method "GET" :uri "/foo" :version "HTTP/1.1" :headers {}})
        (authorised-should= true)))

  (it "sets authorised false for matching uri with no header"
    (-> (map->Request{:method "GET" :uri "/logs" :version "HTTP/1.1" :headers {}})
        (authorised-should= false)))

  (it "sets authorised true for matching uri with correct header"
    (-> (map->Request{:method "GET"
                      :uri "/logs"
                      :version "HTTP/1.1"
                      :headers {"Authorization" (str "Basic " matching-credentials)}})
        (authorised-should= true)))

  (it "sets authorised false for matching uri with invalid syntax header"
    (-> (map->Request{:method "GET"
                      :uri "/logs"
                      :version "HTTP/1.1"
                      :headers {"Authorization" (str "Bas" matching-credentials)}})
        (authorised-should= false)))

  (it "sets authorised false for matching uri with incorrect credentials"
    (-> (map->Request{:method "GET"
                      :uri "/logs"
                      :version "HTTP/1.1"
                      :headers {"Authorization" (str "Basic " (b64/encode-string "admin:hunter3"))}})
        (authorised-should= false)))
)
