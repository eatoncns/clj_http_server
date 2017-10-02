(defproject http-server "0.1.0"
  :description "A basic HTTP server"
  :url "https://github.com/eatoncns/clj_http_server"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main http-server.core
  :aot [http-server.core]
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])
