(defproject metosin/compojure-api-blog "1.0.1"
  :description "Compojure-api-examples"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.11"]]
  :ring {:handler compojure.api.blog.handler/app}
  :uberjar-name "blog-api.jar"
  :uberwar-name "blog-api.war"
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.0"]]}})
