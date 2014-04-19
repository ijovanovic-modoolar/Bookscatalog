(ns Bookscatalog.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css include-js html5]]
        hiccup.core hiccup.form))

(def includes [(include-js "/js/jquery-1.8.2.min.js")
               (include-js "/js/jquery.tablesorter.js")
               (include-js "/js/jquery-ui.js")
               (include-css "/css/reset.css")
               (include-css "/css/style.css")
               (include-css "/css/jquery-ui.css")              
               ])

(defn- build-head [header]
  `[:head
    [:title "Online books catalog in Clojure"]
    ~@includes
    ~@header])

(defn- build-body [content]
  `[:body ~@content])

(defpartial layout
  [& {:keys [header content]}]
  (html5
    (build-head header)
    (build-body content)))

