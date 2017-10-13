# HTTP Server

A basic http server implementation based on [Cob Spec acceptance tests](https://github.com/8thlight/cob_spec).

## Requirements

- [Java](https://www.java.com/en/)
- [Clojure](https://clojure.org/)
- [Leiningen](https://leiningen.org/)
- [Maven](https://maven.apache.org/) for cob spec

## Running

To get code and acceptance tests

    git clone git@github.com:eatoncns/clj_http_server.git
    git clone git@github.com:eatoncns/cob_spec.git

**NOTE:** Due to startup time of clojure jar sometimes exceeding a hardcoded
sleep value in cob spec the above clones my own fork. The only change is to
poll for socket readiness during test startup.

To run the http server on it's own

    cd clj_http_server
    lein run -d <path to cob_spec>/public

The server will start at [localhost:5000](http://localhost:5000)

To run http server unit tests

    cd clj_http_server
    lein spec

To create jar for use by cob spec

    cd clj_http_server
    lein with-profiles production uberjar

This will produce two jar files in /target folder.

To run cob spec server follow instructions on [project page](https://github.com/eatoncns/cob_spec). The jar file to point to is the http-server-0.1.0-standalone.jar generated in the previous step.
