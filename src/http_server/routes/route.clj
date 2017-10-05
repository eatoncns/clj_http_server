(ns http-server.routes.route)

(defprotocol Route
  (is-applicable [this])
  (process [this directory-served]))
