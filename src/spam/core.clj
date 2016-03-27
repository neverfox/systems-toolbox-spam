(ns spam.core
  (:gen-class)
  (:require
    [clj-pid.core :as pid]
    [clojure.core.async :refer [<!! chan]]
    [clojure.string :as string]
    [clojure.tools.cli :refer [parse-opts]]
    [spam.components.middle :as middle]
    [spam.components.query :as query]
    [spam.components.number :as number]
    [spam.config :refer :all]
    [matthiasn.systems-toolbox.switchboard :as sb]
    [taoensso.timbre :refer [refer-timbre]]
    [taoensso.timbre.tools.logging :refer [use-timbre]]))

(use-timbre)
(refer-timbre)

(defn restart! []
  (let [switchboard (sb/component :switchboard)]
    (sb/send-mult-cmd
      switchboard
      [[:cmd/init-comp (middle/cmp-map :middle-cmp)]
       [:cmd/init-comp (query/cmp-map :query-cmp)]
       [:cmd/init-comp (number/cmp-map :number-cmp)]

       [:cmd/route {:from :number-cmp :to :middle-cmp}]
       [:cmd/route {:from :middle-cmp :to :query-cmp}]
       [:cmd/route {:from :query-cmp :to :middle-cmp}]

       [:cmd/send {:to  :number-cmp
                   :msg [:query/numbers]}]])
    switchboard))

(defn start-app [env]
  (let [config (read-config env)]
    (pid/save (:pidfile-name config))
    (pid/delete-on-shutdown! (:pidfile-name config))
    (info (format "Application started in %s mode, PID %s" (name env) (pid/current)))
    (restart!)
    ;; block forever
    (<!! (chan))))

;; CLI

(def cli-options
  [[nil "--env ENV" "Runtime environment"
    :default :dev
    :default-desc "dev"
    :parse-fn keyword
    :validate [#{:dev :prod} "Must be either dev or prod"]]
   ["-h" "--help"]])

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  "Starts the application."
  [& args]
  (let [{:keys [options _ errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 summary)

      errors (exit 1 (error-msg errors)))
    (start-app (:env options))))
