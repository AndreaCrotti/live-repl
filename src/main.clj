(ns main
  (:gen-class)
  (:require
   #_:clj-kondo/ignore
   [integrant.repl.state :as ig-state]
   [integrant.repl :as ir]
   [integrant.core :as ig]
   [system :as s]))

(def config
  {::s/web-server       {:port 3007}
   ::s/nrepl-server     {:port 5553}
   ::s/nrepl-tls-server {:port 5554}
   ::s/prepl-server     {:port 5555}
   ::s/socket-server    {:port 5556}})


(defn start-all []
  (ir/set-prep! #(ig/prep config))
  (ir/prep)
  (ir/init))


(defn -main [& _args]
  (start-all))
