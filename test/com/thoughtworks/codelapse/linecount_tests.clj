(ns com.thoughtworks.codelapse.linecount-tests
  (:use com.thoughtworks.codelapse.linecount clojure.test))

(def cloc-output
  "files,language,blank,comment,code,scale,3rd gen. equiv,\"http://cloc.sourceforge.net v 1.08  T=0.5 s (24.0 files/s, 1206.0 lines/s)\"
10,Bourne Shell,56,155,252,3.81,960.12
2,Python,28,0,112,4.2,470.4")

(deftest test-parsing-language-from-line
  (is (=
    "Bourne Shell"
    (:language (parse-cloc-line "10,Bourne Shell,56,155,252,3.81,960.12")))))

(deftest test-parsing-lines-of-code-from-line
  (is (=
    "252"
    (:lines (parse-cloc-line "10,Bourne Shell,56,155,252,3.81,960.12")))))

(deftest test-parsing-lines-of-comment-from-line
  (is (=
    "155"
    (:comment-lines (parse-cloc-line "10,Bourne Shell,56,155,252,3.81,960.12")))))

(run-tests 'com.thoughtworks.codelapse.linecount-tests)
