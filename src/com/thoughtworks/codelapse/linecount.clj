(ns com.thoughtworks.codelapse.linecount
  (:use com.thoughtworks.codelapse.utils com.thoughtworks.codelapse.fakecode clojure.test midje.semi-sweet))

(defstruct cloc-record :language :lines :comment-lines)

(defn parse-cloc-line [line]
  (let [values (split line ",")]
    (struct-map cloc-record :language (second values) :lines (nth values 4) :comment-lines (nth values 3))))

(defn parse-cloc [cloc-output]
  (map parse-cloc-line (drop 5 (split-lines cloc-output))))

(defn languages
  "Takes a list of maps (cloc records) and returns a set which languages are represented"
  [list-cloc-records]
  (map :language list-cloc-records))

(defn cloc
  [directory]
  "Runs cloc on the given directory, returning the lines of code for each language"
  (parse-cloc (execute "/opt/local/bin/perl" "/Users/admin/Development/codelapse/tools/cloc-1.08.pl" directory "--csv")))

(deftest can-parse-cloc-output
  (expect (parse-cloc "       1 text file.\n       1 unique file.                              \n       0 files ignored.\n\nfiles,language,blank,comment,code,scale,3rd gen. equiv,\"http://cloc.sourceforge.net v 1.08  T=0.5 s (2.0 files/s, 18.0 lines/s)\"\n1,Java,0,0,9,1.36,12.24\n")
    => '({:language "Java" :lines "9" :comment-lines "0"})))

(deftest can-report-on-lines-of-one-file
  (let [temp-dir (make-temp-dir)
        java-file (create-java-file temp-dir "Bob.java" 5)]
    (expect (cloc temp-dir) => '({:language "Java" :lines "9" :comment-lines "0"}))))

(run-tests 'com.thoughtworks.codelapse.linecount)
