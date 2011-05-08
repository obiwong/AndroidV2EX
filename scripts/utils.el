(defun gen-topics-ContentValue.put (name)
   (concat "cv.put(Database.Columns.Topics.NAME_" (upcase name) ", topic." name ");\n"))

(princ-list (mapcar 'gen-topics-ContentValue.put names))
