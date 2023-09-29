(ns config
  (:require
   [clojure.java.io :as io]
   [aero.core :as a]))

(defn config []
  (->> "config.edn"
       io/resource
       a/read-config))
