(defproject resume "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [enlive "1.1.6"]
                 [jeremys/cljss-core "0.3.0"]
                 [jeremys/cljss-units "0.2.2"]
                 [jeremys/cljss-grid "0.1.0"]
                 ]



  :clean-targets [:target-path "out" "resume.html"]

  :main resume.core

  :profiles {:dev {:dependencies [[org.clojure/clojurescript "1.7.170"]]

                   :plugins [[lein-cljsbuild "1.1.1"]
                             [lein-figwheel "0.5.0-1"]]

                   :cljsbuild {:builds
                               [{:id "dev"
                                 :source-paths ["src"]

                                 :figwheel {:on-jsload "resume.core/on-js-reload"}

                                 :compiler {:main resume.core
                                            :asset-path "js/compiled/out"
                                            :output-to "out/js/main.js"
                                            :output-dir "out/js/compiled/out"
                                            :source-map-timestamp true}}]}

                   :figwheel {;; :http-server-root "out"
                              ;; :server-port 3449 ;; default
                              ;; :server-ip "127.0.0.1"
                              :css-dirs ["out/css"] ;; watch and update CSS

                              ;; Start an nREPL server into the running figwheel process
                              ;; :nrepl-port 7888

                              ;; Server Ring Handler (optional)
                              ;; if you want to embed a ring handler into the figwheel http-kit
                              ;; server, this is for simple ring servers, if this
                              ;; doesn't work for you just run your own server :)
                              ;; :ring-handler hello_world.server/handler

                              ;; To be able to open files in your editor from the heads up display
                              ;; you will need to put a script on your path.
                              ;; that script will have to take a file path and a line number
                              ;; ie. in  ~/bin/myfile-opener
                              ;; #! /bin/sh
                              ;; emacsclient -n +$2 $1
                              ;;
                              ;; :open-file-command "myfile-opener"

                              ;; if you want to disable the REPL
                              ;; :repl false

                              ;; to configure a different figwheel logfile path
                              ;; :server-logfile "tmp/logs/figwheel-logfile.log"
                              }}}

  )
