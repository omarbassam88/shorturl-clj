(ns shorturl.db
  (:require [clojure.java.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers
             :refer
             [insert-into columns values select
              from where delete-from]]))

(def db {:classname   "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname     "db/db.sqlite"})

(defn query [q]
  (jdbc/query db q))

(defn insert-redirect! [slug url]
  (jdbc/execute! db (-> (insert-into :redirects)
                        (columns :slug :url)
                        (values
                         [[slug url]])
                        (sql/format))))

(defn get-url [slug]
  (-> (query (-> (select :url)
                 (from :redirects)
                 (where [:= :slug slug])
                 (sql/format)))
      first
      :url))

(defn clear-db []
  (jdbc/execute! db (-> (delete-from :redirects)
                        (sql/format))))

(comment
  (query (-> (select :*)
             (from :redirects)
             (sql/format)))
  (insert-redirect! "ob88" "http://ob88.dev")
  (get-url "ob88")
  (clear-db)
  ;;
  )
