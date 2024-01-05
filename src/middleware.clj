(ns middleware
  (:require
   [clojure.tools.logging :as log]))

#_:clj-kondo/ignore
(defn log-action
  "Sample middleware function to log all the code evaluated"
  [h]
  (fn [{:keys [op code] :as msg}]
    (when (= "eval" op)
      ;; replace this with your own logging function
      (log/info "evaluating code " code))
    (h msg)))
