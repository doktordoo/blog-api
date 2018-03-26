(ns compojure.api.blog.domain
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [schema.core :as s]
            [clojure.tools.logging :as log]
            [clojure.java.jdbc :as      sql]
            [ring.swagger.schema :refer [coerce!]]))

;; Database
(def db-config
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "mem:bposts"
   :user ""
   :password ""})

   (defn pool
    [config]
        (let [cpds (doto (ComboPooledDataSource.)
                     (.setDriverClass (:classname config))
                     (.setJdbcUrl (str "jdbc:" (:subprotocol config) ":" (:subname config)))
                     (.setUser (:user config))
                     (.setPassword (:password config))
                     (.setMaxPoolSize 6)
                     (.setMinPoolSize 1)
                     (.setInitialPoolSize 1))]
          {:datasource cpds}))

(def pooled-db (delay (pool db-config)))

(defn db-connection [] @pooled-db)

(sql/db-do-commands (db-connection)
(log/debug "create database")
; (sql/drop-table :posts) ; no need to do that for in-memory databases
;(sql/db-do-commands (db-connection) ["create sequence posts_seq"])
(sql/create-table-ddl :posts [[:id "bigint primary key"]
                              [:title "varchar(256)"]
                              [:categories :varchar]
                              [:content :varchar]
                              [:created "timestamp default CURRENT_TIMESTAMP"]]))

;; Domain
(s/defschema Post {:id Long
  :title String
  :content   String
  ;;:categories #{s/Str}
  :categories String
  (s/optional-key :created) java.sql.Timestamp
  })

(s/defschema NewPost (dissoc Post :id))

;; Repository
(defonce id-seq (atom 0))
(defn get-post-id [] 
  (sql/query (db-connection)
      ["select posts_seq.nextval"]))
;; Blog
(defonce posts (atom (array-map)))

;; (defn get-post [id] (log/info "get-post" (@posts id))(@posts id))

(defn  get-post [id]
  (log/info "get-post" id)
  (sql/query (db-connection)
      ["select * from posts where id = ?" id]
      {:result-set-fn first}))

(defn get-posts-count []
  (sql/query (db-connection) ["select count(1) from posts"]))

(defn get-posts []
  (log/info "get-posts")
  (sql/query (db-connection) ["select * from posts"]))

;; (defn get-posts []  (log/info "get-posts") (-> posts deref vals reverse))
;; (defn delete! [id]  (log/info "delete-post" id) (swap! posts dissoc id) nil)
(defn delete! [id]
  (log/info "delete-post" id)
  (sql/delete! (db-connection) :posts ["id=?" id]) nil)

;;(defn add! [new-post]
;;  (log/info "add post" new-post)
;;  (let [id (swap! id-seq inc)
;;        post (coerce! Post (assoc new-post :id id))]
;;    (swap! posts assoc id post)
;;    post))

(defn add! [new-post]
      (log/info "add post" new-post)
      (let [id (swap! id-seq inc)]
        (sql/insert! (db-connection) :posts (assoc new-post "id" id))
        (get-post id)))

;;(defn update! [post]
;;  (log/info "add post" post)
;;  (let [post (coerce! Post post)]
;;    (swap! posts assoc (:id post) post)
;;    (get-post (:id post))))

(defn update! [post]
      (log/info "update post" post)
      (let [id (:id post)]
      (sql/update! (db-connection) :documents ["id=?" (:id post)] post)
      (get-post id)))

;; Data
 ;;(when (empty? @posts)
 (when (not= get-posts-count "0")
   (log/info "initialize database")
   (add! { :title "Hi!"  :content "Post about Friends"  :categories "Computer, Friends"})
   (add! { :title "New Post"  :content "Post about Candy"  :categories "Candy"})
   (add! { :title "New Post2"  :content "2nd post about candy candy"  :categories "Candy"}))
