(ns spam.components.number
  (:require [taoensso.timbre :refer [refer-timbre]]))

(refer-timbre)

(defn get-numbers
  [{:keys [put-fn]}]
  (info "Getting numbers")
  (map #(put-fn [:data/number %]) (range 2000)))

(defn mk-state
  [_]
  (info "Number component started")
  {:state (atom nil)})

(defn cmp-map
  [cmp-id]
  {:cmp-id      cmp-id
   :state-fn    mk-state
   :handler-map {:query/numbers get-numbers}})
