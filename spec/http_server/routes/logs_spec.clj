(ns http-server.routes.logs-spec
  (:require [speclj.core :refer :all]
            [http-server.routes.logs :refer :all]
            [http-server.spec-helper :refer [applicable-should=]]
            [http-server.routes.route :as route]
            [http-server.constants.methods :refer :all]
            [clojure.set :as s]))

(defn logs-request [method uri]
  (map->Logs {:request {:method method :uri uri}}))

(describe "is-applicable?"
  (it "returns true for GET to /logs"
    (applicable-should= (logs-request GET "/logs") true))

  (it "returns false for GET to other uri"
    (applicable-should= (logs-request GET "/log") false))

  (it "returns false for methods other than GET"
    (doseq [method (s/difference http-methods #{GET})]
      (applicable-should= (logs-request method "/logs") false)))
)
