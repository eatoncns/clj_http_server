(ns http-server.spec-helper
  (:require [speclj.core :refer [should=]]))

(defn rshould= [actual expected]
  (should= expected actual))
