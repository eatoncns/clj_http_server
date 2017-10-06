(ns http-server.utils.html)

(defn- add-list-item [current-list filename]
  (str current-list "<li><a href=\"/" filename "\">" filename "</a></li>"))

(defn list-of-links [filenames]
  (-> (reduce add-list-item "<ul>" filenames)
      (str "</ul>")))
