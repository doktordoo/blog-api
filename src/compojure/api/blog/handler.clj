(ns compojure.api.blog.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [compojure.api.blog.domain :refer :all]
            [schema.core :as s]))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Blog Api"
                    :description "Compojure Api Blog application"}
             :tags [{:name "post", :description "blog post Api it is."}]}}}
        
    (context "/posts" []
          :tags ["post"]
                
      (GET "/" []
            :return [Post]
            :summary "Gets all blog Posts"
            (ok (get-posts)))
        
      (POST "/" []
            :return Post
            :body [post NewPost {:description "new blog post"}]
              :summary "Creates a new blog post entry. Returns the created blog post with the ID"
              (ok (add! post)))

      (PUT "/" []
            :return Post
            :body [post Post]
            :summary "Updates a blog post"
            (ok (update! post)))
        
      (GET "/:id" []
            :return Post
            :path-params [id :- Long]
            :summary "Creates a new blog post entry. Returns the created blog post with the ID"
            (ok (get-post id)))
        
      (DELETE "/:id" []
            :path-params [id :- Long]
            :summary "Deletes a single blog post with the given ID. Returns the post"
            (ok (delete! id)))
        )))