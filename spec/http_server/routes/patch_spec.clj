(ns http-server.routes.patch-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.patch :refer :all]
            [http-server.routes.route :as route]
            [http-server.request :refer [map->Request]]
            [http-server.utils.file-info :as fi]
            [http-server.utils.functional :as func]
            [http-server.utils.sha1 :as sha1]
            [http-server.spec-helper :refer [applicable-should=]]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn patch-request [method uri]
  (map->Patch {:request {:method method :uri uri}}))

(defrecord FakeFileInfo []
  fi/FileInfo
  (file-exists? [this path] (= path "/foo.txt"))
  (file-data [this path] (.getBytes "blah"))
  (patch-file [this path data] nil))

(defn run-patch [request]
  (->> (FakeFileInfo.)
       (process-patch request)))

(defn response-should-have [request field expected]
  (->> request
       (run-patch)
       (field)
       (should= expected)))

(describe "is-applicable?"
  (it "returns true for PATCH"
    (applicable-should= (patch-request PATCH "/patch.txt") true))

  (it "returns false for methods other than PATCH"
    (doseq [method (s/difference http-methods #{PATCH})]
      (applicable-should= (patch-request method "/patch.txt") false)))
)

(describe "process-patch"
  (it "returns 404 status when uri is not an existing file"
    (-> (map->Request {:uri "/nope.txt" :headers {}})
        (response-should-have :status 404)))

  (it "returns a 422 status when file exists but no If-Match present"
    (-> (map->Request {:uri "/foo.txt" :headers {}})
        (response-should-have :status 422)))

  (it "returns a 412 if If-Match condition fails"
    (-> (map->Request {:uri "/foo.txt" :headers {"If-Match" "ahashthatdoesntmatch"}})
        (response-should-have :status 412)))

  (it "returns a 204 if patch succeeds"
    (-> (map->Request {:uri "/foo.txt"
                        :headers {"If-Match" (sha1/encode-str "blah")}
                        :body "updated"})
        (response-should-have :status 204)))

  (it "returns ETag header with new hash if patch succeeds"
    (->> (map->Request {:uri "/foo.txt"
                        :headers {"If-Match" (sha1/encode-str "blah")}
                        :body "updated"})
         (run-patch)
         ((func/flip get-in) [:headers "ETag"])
         (should= (sha1/encode-str "updated"))))
)
