(ns http-server.spec-helper
  (:require [speclj.core :refer [should= should-be-a]]))

(defn rshould= [actual expected]
  (should= expected actual))

(defn rshould-be-a [actual expected]
  (should-be-a expected actual))
