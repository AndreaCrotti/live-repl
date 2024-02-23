(ns system
  (:require
   [cheshire.core :as json]
   [clojure.core.server :as server]
   [config :refer [config]]
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   #_:clj-kondo/ignore
   [middleware :as m]
   [nrepl.server :as nrepl]))


(defmethod ig/init-key ::nrepl-server [_ {:keys [port]}]
  (assert (some? port), "Missing port")
  (println "Starting nrepl server on port " port)
  (nrepl/start-server :port port
                      :bind "0.0.0.0"))


(defmethod ig/halt-key! ::nrepl-server [_ repl]
  (nrepl/stop-server repl))

(defmethod ig/init-key ::nrepl-with-middleware [_ {:keys [port]}]
  (assert (some? port), "Missing port")
  (println "Starting nrepl server on port " port)
  (nrepl/start-server :port port
                      :bind "0.0.0.0"
                      :handler (nrepl/default-handler 'middleware/log-action)))

(defmethod ig/halt-key! ::nrepl-with-middleware [_ repl]
  (nrepl/stop-server repl))

(defmethod ig/init-key ::nrepl-tls-server [_ {:keys [port]}]
  (assert (some? port), "Missing port")
  (println "Starting nrepl-tls server on port " port)
  (nrepl/start-server :port port
                      :tls? true
                      ;; see https://nrepl.org/nrepl/usage/tls.html for the key generation
                      :tls-keys-file "nrepl_tls.keys"
                      :bind "0.0.0.0"))


(defmethod ig/halt-key! ::nrepl-tls-server [_ repl]
  (nrepl/stop-server repl))


(defmethod ig/init-key ::prepl-server
  [_ {:keys [port]}]
  (assert (some? port), "missing prepl server port")
  ;; somehow failing to listen on the same port
  (println "Starting prepl server on port " port)
  (server/start-server {:accept  'clojure.core.server/io-prepl
                        :address "127.0.0.1"
                        :port    port
                        :name    :prepl}))

(defmethod ig/halt-key! ::prepl-server
  [_ _]
  ;; see if we can get the name from the socket object?
  (server/stop-server :prepl))

(defmethod ig/init-key ::socket-server
  [_ {:keys [port]}]
  (assert (some? port), "missing socket server port")
  (println "Starting prepl server on port " port)
  (server/start-server {:accept  'clojure.core.server/repl
                        :address "127.0.0.1"
                        :port    port
                        :name    :socket}))

(defmethod ig/halt-key! ::socket-server
  [_ _]
  ;; see if we can get the name from the socket object?
  (server/stop-server :socket))

(defn health-check
  [_request]
  {:status 200, :body "ok"})

(defn echo
  [request]
  {:status 200, :body request})

(defn conf
  [_request]
  {:status  200
   :body    (json/encode (config))
   :headers {"Content-Type" "application/json"}}
)
(def routes
  #{["/health" :get `health-check :route-name :health]
    ["/echo" :get `echo :route-name :echo]
    ["/config" :get `conf :route-name :config]})

(defn service-map [port]
  {::http/routes          routes
   ::http/resource-path   "/public"
   ::http/type            :jetty
   ::http/join?           false
   ::http/host            "0.0.0.0"
   ::http/allowed-origins {:creds           true
                           :allowed-origins (constantly true)}
   ::http/port            port})

(defmethod ig/init-key ::web-server
  [_ {:keys [port]}]
  (-> (service-map port)
      http/default-interceptors
      http/create-server
      http/start))

(defmethod ig/halt-key! ::web-server
  [_ server]
  (http/stop server))
