(ns spam.components.query
  (:require [clojure.core.async :refer [<! chan go-loop pipe put!]]
            [taoensso.timbre :refer [refer-timbre]]))

(refer-timbre)

(defn get-number
  [{:keys [cmp-state msg-payload]}]
  (let [{:keys [in-ch]} @cmp-state]
    (info "Getting number" msg-payload)
    (put! in-ch msg-payload)))

(defn out-loop
  [put-fn ch]
  (go-loop []
    (let [msg-payload (<! ch)]
      (info "Returning number" msg-payload)
      (put-fn [:result/number msg-payload])
      (recur))))

(defn- mk-state
  [put-fn]
  (let [in-ch (chan)
        out-ch (chan)]
    (info "Query compontent started")
    ;(pipe in-ch out-ch)
    (out-loop put-fn out-ch)
    {:state (atom {:in-ch  in-ch
                   :out-ch out-ch})}))

(defn cmp-map
  [cmp-id]
  {:cmp-id      cmp-id
   :state-fn    mk-state
   :handler-map {:query/number get-number}})
