(ns system-test
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :refer [response-for]]
            [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [routes]
            [system]
            [pedestal]))

(def url-for (route/url-for-routes
              (route/expand-routes routes/routes)))

(defn service-fn
  [system]
  (get-in system [:pedestal :service ::http/service-fn]))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(deftest greeting-test
  (with-system [sut (system/new-system :test)]
    (let [service               (service-fn sut)
          {:keys [status body]} (response-for service
                                              :get
                                              (url-for :ahoy))]
      (is (= 200 status))
      (is (= "Hello, world! Today's menu Coq au Vin!" body)))))
