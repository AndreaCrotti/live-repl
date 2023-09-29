(defun remote-repl ()
  (interactive)
  ;; could use variables but the "'" is doing the quoting here
  (cider-connect '(:host "localhost" :port 5553)))

(defun portal.api/open ()
  (interactive)
  (cider-nrepl-sync-request:eval
   "(do (ns dev) (def portal ((requiring-resolve 'portal.api/open) {:theme :portal.colors/solarized-dark})) (add-tap (requiring-resolve 'portal.api/submit)))"))

(defun portal.api/clear ()
  (interactive)
  (cider-nrepl-sync-request:eval "(portal.api/clear)"))

(defun portal.api/close ()
  (interactive)
  (cider-nrepl-sync-request:eval "(portal.api/close)"))
