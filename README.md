# Redux Blog Post API 
Example Blog Post api for Udemy course Modern React with Redux course (section 5)
Created from from Metosin Compojure-api-examples
Project for using [Compojure-api](https://github.com/metosin/compojure-api).

## Usage

### Running

`lein ring server`

### Packaging and running as standalone jar

```
lein do clean, ring uberjar
java -jar target/examples.jar
```

### Packaging as war

`lein ring uberwar`

## License

Copyright © 2014-2015 [Metosin Oy](http://www.metosin.fi)

Distributed under the Eclipse Public License, the same as Clojure.
