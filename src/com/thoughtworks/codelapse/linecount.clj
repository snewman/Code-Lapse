(ns com.thoughtworks.codelapse.linecount)

(defstruct cloc-record :language :lines :comment-lines)

(defn split [str delimiter]
  (seq (.split str delimiter)))

(defn split-lines [str]
  (seq (.split #"\r?\n" str)))

(defn parse-cloc-line [line]
  (let [values (split line ",")]
    (struct-map cloc-record :language (second values) :lines (nth values 4) :comment-lines (nth values 3))))

(defn parse-cloc [cloc-output]
  (map parse-cloc-line (next (split-lines cloc-output))))

(defn languages
  "Takes a list of maps (cloc records) and returns a set which languages are represented"
  [list-cloc-records]
  (map :language list-cloc-records))
