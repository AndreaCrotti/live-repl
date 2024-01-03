(ns middleware)

(defn log-action
  "Sample middleware function to log all the code evaluated"
  [h]
  (fn [{:keys [op code] :as msg}]
    (when (= "eval" op)
      ;; TODO: use the actual logging functions
      (println "evaluating code " code))
    (h msg)))
