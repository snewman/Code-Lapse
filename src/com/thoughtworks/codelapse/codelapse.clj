(ns com.thoughtworks.codelapse.codelapse
  (:use com.thoughtworks.codelapse.linecount midje.semi-sweet clojure.test))

(declare cloc)


; Whe run on some (known) directory create an expected output
(deftest analysis-of-single-commit
  (expect))

(defn class-header
  [classname]
  (str "public static class " classname " {\n"))

(def method-header
  "public static void main(String[] args) {\n")

(def footer "}\n}\n")

(defn lines-of-java
  [number]
  (apply str (take number (repeat "System.currentTimeMillis();"))))

(defn java-file
  [classname lines-of-code]
  (str (class-header "Bob") method-header (lines-of-java lines-of-code)) footer)

(use-fixtures :each with-example-code-directory)
