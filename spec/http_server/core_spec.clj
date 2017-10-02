(ns http-server.core-spec
  (:require [speclj.core :refer :all]
            [http-server.core :refer :all]))

(describe "get-opts"
  (it "parses port from args"
    (let [options (get-opts ["-p" "3000"])]
      (should= 3000 (:port options))))

  (it "defaults to port 5000"
    (let [options (get-opts [])]
      (should= 5000 (:port options))))

  (it "parses directory from args"
    (let [options (get-opts ["-d" "/blah/"])]
      (should= "/blah/" (:directory options))))
)
