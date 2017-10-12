(ns http-server.routes.route)

(defprotocol Route
  (is-applicable? [this directory-served])
  (process [this directory-served]))
