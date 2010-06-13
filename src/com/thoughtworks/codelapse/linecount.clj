(ns com.thoughtworks.codelapse.linecount)

(defstruct cloc-record :language :lines :comment-lines)

(defn split [str delimiter]
  (seq (.split str delimiter)))

(defn parse-cloc-line [line]
  (let [values (split line ",")]
    (struct cloc-record (second values) (nth values 4) (nth values 3))))
