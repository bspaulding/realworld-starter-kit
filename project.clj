(defproject realworld "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.946"]
                 [com.cemerick/piggieback "0.2.1"]
                 [binaryage/devtools "0.9.9"]
                 [org.omcljs/om "1.0.0-beta1"]
                 [sablono "0.8.3"]
                 [markdown-clj "1.0.2"]
                 [compassus "1.0.0-alpha3"]
                 #_[com.datomic/datomic-pro "0.9.5561.56"]

                 ;; from vase template
                 [io.pedestal/pedestal.service "0.5.2"]
                 [com.cognitect/pedestal.vase "0.9.1"]

                 ;; Remove this line and uncomment one of the next lines to
                 ;; use Immutant or Tomcat instead of Jetty:
                 [io.pedestal/pedestal.jetty "0.5.2"]
                 ;; [io.pedestal/pedestal.immutant "0.5.2-SNAPSHOT"]
                 ;; [io.pedestal/pedestal.tomcat "0.5.2-SNAPSHOT"]

                 [ch.qos.logback/logback-classic "1.1.8" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.22"]
                 [org.slf4j/jcl-over-slf4j "1.7.22"]
                 [org.slf4j/log4j-over-slf4j "1.7.22"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.15"]]
  :main ^{:skip-aot true} realworld.server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[figwheel-sidecar "0.5.15"]
                                  [io.pedestal/pedestal.service-tools "0.5.2"]]
                   :source-paths ["src-cljs"]
                   :aliases {"run-dev" ["trampoline" "run" "-m" "realworld.server/run-dev"]}}}
  ;; :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :cljsbuild
    {:builds [{:id "dev"
                :source-paths ["src-cljs"]
                :figwheel true
                :compiler {:output-to "resources/public/js/main.js"
                           :optimizations :none
                           :main "realworld.core"
                           :asset-path "js/out"
                           :source-map true
                           :preloads [devtools.preload]
                           :pretty-print true}}]})
