(ns http-server.spec-helper
  (:require [speclj.core :refer [should= should-be-a]]
            [http-server.routes.route :as route]))

(defn rshould= [actual expected]
  (should= expected actual))

(defn rshould-be-a [actual expected]
  (should-be-a expected actual))

(defn applicable-should= [route-instance expected]
  (-> route-instance
      (route/is-applicable? "directory-served")
      (rshould= expected)))

(defn response-should-have [route-instance field expected]
  (-> route-instance
      (route/process "directory served")
      (get field)
      (rshould= expected)))

(defn response-should-have-header [route-instance header expected]
  (-> route-instance
      (route/process "directory served")
      (get-in [:headers header])
      (rshould= expected)))
