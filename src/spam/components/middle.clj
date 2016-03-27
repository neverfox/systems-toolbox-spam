(ns spam.components.middle
  (:require [taoensso.timbre :refer [refer-timbre]]))

(refer-timbre)

(defn check-number
  [{:keys [msg-payload put-fn]}]
  (info "Checking number" msg-payload)
  (put-fn [:query/number msg-payload]))

(defn receive-number
  [{:keys [msg-payload]}]
  (info "Receiving" msg-payload))

(defn mk-state
  [_]
  (info "Middle compontent started")
  {:state (atom nil)})

(defn cmp-map
  [cmp-id]
  {:cmp-id      cmp-id
   :state-fn    mk-state
   :handler-map {:data/number check-number
                 :result/number receive-number}})
