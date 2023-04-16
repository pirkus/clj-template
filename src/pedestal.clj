(ns pedestal
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defrecord Pedestal [service-map
                     service]
  component/Lifecycle
  (start [this]
    (if service
      this
      (-> service-map
          http/create-server
          http/start
          ((partial assoc this :service)))))
  (stop [this]
    (http/stop service)
    (assoc this :service nil)))

(defn new-pedestal
  []
  (map->Pedestal {}))

