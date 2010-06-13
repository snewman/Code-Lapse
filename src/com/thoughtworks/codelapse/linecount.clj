(ns com.thoughtworks.codelapse.linecount)

(defstruct cloc-record :language)

(defn split [str delimiter]
  (seq (.split str delimiter)))

(defn parse-cloc-line [line]
  (struct cloc-record (second (split line ","))))
