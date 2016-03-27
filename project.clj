(defproject systems-toolbox-spam "0.1.0-SNAPSHOT"
  :description "Geting spammed with messages in systems-toolbox"
  :url "https://github.com/neverfox/systems-toolbox-spam"
  :dependencies [[aero "0.2.0"]
                 [clj-pid "0.1.2"]
                 [com.taoensso/timbre "4.3.1" :exclusions [io.aviso/pretty]]
                 [matthiasn/systems-toolbox "0.5.14"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.374"]
                 [org.clojure/tools.cli "0.3.3"]
                 [org.clojure/tools.logging "0.3.1"]]
  :main ^:skip-aot spam.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot          :all
                       :uberjar-name "systems-toolbox-spam-standalone.jar"}
             :dev     {:plugins      [[lein-midje "3.2"]
                                      [lein-set-version "0.4.1"]
                                      [lein-update-dependency "0.1.2"]]
                       :dependencies [[midje "1.8.3" :exclusions [clj-time
                                                                  clj-tuple
                                                                  potemkin
                                                                  riddley]]
                                      [pjstadig/humane-test-output "0.8.0"]]
                       :source-paths ["src"]
                       :injections   [(require 'pjstadig.humane-test-output)
                                      (pjstadig.humane-test-output/activate!)]}}
  :repositories {"my.datomic.com" {:url   "https://my.datomic.com/repo"
                                   :creds :gpg}})
