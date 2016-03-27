(ns spam.config
  (:require [aero.core :as aero]))

(defn read-config
  ([] (read-config :default))
  ([profile]
   (aero/read-config "resources/config.edn" {:profile profile})))
