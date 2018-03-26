(defproject compojure-api-blog "1.0.0-SNAPSHOT"
  :description "Compojure-api-examples"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "2.0.0-alpha18"]
                 [org.clojure/tools.logging "0.4.0"]
                 [com.mchange/c3p0 "0.9.5.2"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [com.h2database/h2 "1.3.176"]]
  :ring {:handler compojure.api.blog.handler/app}
  :uberjar-name "blog-api.jar"
  :uberwar-name "blog-api.war"
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.0"]]}})
