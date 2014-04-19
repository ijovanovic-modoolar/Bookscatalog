(ns Bookscatalog.models.backend
  (:require
    [clojure.java.jdbc :as j]
    [clojure.java.jdbc.sql :as s]
 ))

(def db {
          :subprotocol "mysql"
          :subname "//127.0.0.1:3306/books"
          :user "root"
          :password "admin" } )


(defn recreate-table
  "Drops table ( if exists ) and recreates it"
  [name & specs]
  (try
    (j/drop-table name )
    (catch Exception ignore ) )
  (apply j/create-table name specs ) )


(defn clean-create-tables
  "Drops and recreate tables"
  []
  (j/with-connection db
    (doseq []
      (recreate-table "books" [:id :int "PRIMARY KEY AUTO_INCREMENT"] 
                              [:author "VARCHAR(255)"]
                              [:title "VARCHAR(255)"]
                              [:release_date "DATE"]
                              [:format "VARCHAR(255)"]))))

(defn add-book
  "Add new book"
  ^String [author title release_date format]
  (j/insert! db :books
    { :author author :title title :release_date release_date :format format} ) )


(defn get-books
  "Retrieves information about books"
  []
 (j/with-connection db
     (j/with-query-results rs ["select * from books"]
       (doall rs))))

(defn books []
  (get-books)  
)
