(ns http-server.routes.patch-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.patch :refer :all]
            [http-server.routes.route :as route]
            [http-server.request :refer [map->Request]]
            [http-server.utils.file-info :as fi]
            [http-server.utils.functional :as func]
            [http-server.utils.sha1 :as sha1]))

(defrecord FakeFileInfo []
  fi/FileInfo
  (file-exists? [this path] (= path "/foo.txt"))
  (file-data [this path] (.getBytes "blah"))
  (patch-file [this path data] nil))

(defn run-patch [request]
  (->> (FakeFileInfo.)
       (process-patch request)))

(describe "is-applicable?"
  (it "returns true for PATCH"
    (-> (map->Patch{:request {:method "PATCH" :uri "/patch.txt"}})
        (route/is-applicable? "directory-served")
        (should= true)))

  (it "returns false for methods other than PATCH"
    (doseq [method ["GET" "POST" "HEAD" "PUT" "OPTIONS"]]
      (-> (map->Patch{:request {:method method :uri "/patch.txt"}})
          (route/is-applicable? "directory-served")
          (should= false))))
)

(describe "process-patch"
  (it "returns 404 status when uri is not an existing file"
    (->> (map->Request {:uri "/nope.txt" :headers {}})
         (run-patch)
         (:status)
         (should= 404)))

  (it "returns a 422 status when file exists but no If-Match present"
    (->> (map->Request {:uri "/foo.txt" :headers {}})
         (run-patch)
         (:status)
         (should= 422)))

  (it "returns a 412 if If-Match condition fails"
    (->> (map->Request {:uri "/foo.txt" :headers {"If-Match" "ahashthatdoesntmatch"}})
         (run-patch)
         (:status)
         (should= 412)))

  (it "returns a 204 if patch succeeds"
    (->> (map->Request {:uri "/foo.txt"
                        :headers {"If-Match" (sha1/encode-str "blah")}
                        :body "updated"})
         (run-patch)
         (:status)
         (should= 204)))

  (it "returns ETag header with new hash if patch succeeds"
    (->> (map->Request {:uri "/foo.txt"
                        :headers {"If-Match" (sha1/encode-str "blah")}
                        :body "updated"})
         (run-patch)
         ((func/flip get-in) [:headers "ETag"])
         (should= (sha1/encode-str "updated"))))

)
