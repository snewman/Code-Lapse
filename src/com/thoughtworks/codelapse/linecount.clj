(ns com.thoughtworks.codelapse.linecount)

(defstruct cloc-record :language :lines :comment-lines)

(defn split [str delimiter]
  (seq (.split str delimiter)))

(defn parse-cloc-line [line]
  (let [values (split line ",")]
    (struct-map cloc-record :language (second values) :lines (nth values 4) :comment-lines (nth values 3))))
