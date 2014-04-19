(ns Bookscatalog.views.books
  (:require [Bookscatalog.models.backend :as db]
            [Bookscatalog.views.common :as common])
  (:use [noir.core :only [defpage]]
        [noir.response :only [content-type]]
        [hiccup.element ]
        hiccup.core hiccup.form
        hiccup.def
        hiccup.util
        ))

(defn- list-books []
  [:table.tablesorter {:id "bookTable"}
   [:thead
    [:tr
     [:th "Author"]
     [:th "Title"]
     [:th "Published"]
     [:th "Format"]
     ]]
   (into [:tbody]
         (for [book (db/books)]
           [:tr
            [:td (:author book)]
            [:td (:title book)]
            [:td (:release_date book)]
            [:td (image (str "/img/"(:format book) ".png"))]]))])
(defpage "/" []
  (common/layout
    :content [[:h1 "Welcome to online book catalog"]
              [:p "Please select one of available options:"]
              [:ul
              [:li (link-to "/init_db" "Initialize database (first time only) ")]
              [:li (link-to "/add_book" "Add new book ")]
              [:li (link-to "/books" "View books catalog ")]]]))

(defpage "/books" []
  (common/layout
    :header [(javascript-tag "$(document).ready(function() {$(\"#bookTable\").tablesorter();});")]
    :content [[:h1 "Online books catalog"]
              [:p "Here you can find a list of all book records in database. You can sort table per each column. To add a new book, user Add new book option"]
              (list-books)
              (link-to "/add_book" (image  "/img/add_file.png") "Add new book")]))

(defpage "/init_db" 
  []
  (common/layout
    :content [
              [:h1 "Online Book catalog database initialization"]
              (do
				        (try
				          (db/clean-create-tables)
                   [:p "Database initialized successfully. "
                       (link-to "/add_book" "Add your first book here")
                   ]
				          (catch Exception e
				            {:message (str "Error: " (.getMessage e))}
				          )))]))

(defpage "/add_book" {:keys [message status]}
  (common/layout
    :content [   
				    (form-to [:post "/add_book"]
                 [:div {:class "wrap"}
                  ;Message to display to user as a operation result
						      (if message [:h2 message])
		              (if (= "OK" status) [:h2 (link-to "/books" "Go to books catalog")])
                  [:div {:class "input-left"}
                   (label {:class "input-left"} "author" "Author")
				           (label {:class "input-left"} "title" "Title")
				           (label {:class "input-left"} "release_date" "Release date")
				           (label {:class "input-left"} "format" "Format")
                    
				          ]
                  [:div {:class "input-right"}
                    (text-field {:class "input-right"} "author")
							      (text-field {:class "input-right"} "title")
							      (text-field {:class "input-right" :id "datepicker"} "release_date")
                    (javascript-tag "$( '#datepicker' ).datepicker({ dateFormat: \"yy-mm-dd\" });")
							      (drop-down {:class "input-right"} "format" ["PDF" "DOC"] )
                    (submit-button "Add book")
                   ]
				      ]
				      )]))
				 
(defpage [:post "/add_book"] {:keys [author title release_date format]}
  (noir.core/render "/add_book"
    (do
        (try
          (db/add-book author title release_date format)
          {:message "Successfull!" :status "OK"}
          (catch Exception e
            {:message (str "Error: " (.getMessage e)) :status "NOK"}
          )
        )
     )
    )
  )