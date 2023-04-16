(ns routes)

(defn respond-hello [request]
  {:status 200 :body "Hello, world! Today's menu Coq au Vin!"})

(def routes
  #{["/" :get respond-hello :route-name :ahoy]})

