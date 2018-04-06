(defproject realworld "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [com.cemerick/piggieback "0.2.1"]
                 [binaryage/devtools "0.9.9"]
                 [org.omcljs/om "1.0.0-beta1"]
                 [sablono "0.8.3"]
                 [markdown-clj "1.0.2"]
                 #_[com.datomic/datomic-pro "0.9.5561.56"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.15"]]
  :main ^:skip-aot realworld.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[figwheel-sidecar "0.5.15"]]
                   :source-paths ["src-cljs"]}}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
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
