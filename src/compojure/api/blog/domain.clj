(ns compojure.api.blog.domain
  (:require [schema.core :as s]
            [ring.swagger.schema :refer [coerce!]]))

;; Domain
(s/defschema Post {:id    Long
  :title String
  :content   String
  :categories #{s/Str}
;;  :created java.util.Date
  })

(s/defschema NewPost (dissoc Post :id))

;; Repository
(defonce id-seq (atom 0))

;; Blog
(defonce posts (atom (array-map)))

(defn now [] (new java.util.Date))

(defn get-post [id] (@posts id))
(defn get-posts [] (-> posts deref vals reverse))
(defn delete! [id] (swap! posts dissoc id) nil)

(defn add! [new-post]
  (let [id (swap! id-seq inc)
        post (coerce! Post (assoc new-post :id id))]
    (swap! posts assoc id post)
    post))

(defn update! [post]
  (let [post (coerce! Post post)]
    (swap! posts assoc (:id post) post)
    (get-post (:id post))))

;; Data
 (when (empty? @posts)
   (add! { :title "Hi!"  :content "Post about Friends"  :categories ["Computer, Friends"]})
   (add! { :title "New Post"  :content "Post about Candy"  :categories ["Candy"]})
   (add! { :title "New Post2"  :content "2nd post about candy candy"  :categories ["Candy"]}))
