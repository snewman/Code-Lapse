(ns com.thoughtworks.codelapse.fakecode
  (:use com.thoughtworks.codelapse.utils midje.semi-sweet clojure.test clojure.contrib.io)
  (:import
    (java.io File)))

(defn class-header
  [classname]
  (str "public static class " classname " {\n"))

(def method-header
  "public static void main(String[] args) {\n")

(def footer "}\n}\n")

(defn lines-of-java
  [number]
  (apply str (take number (repeat "System.currentTimeMillis();\n"))))

(defn java-class
  [classname lines-of-code]
  "Creates a named class with the given number of lines of code. Note that due to class and
  method declarations 4 is the minimum length"
  (if (< lines-of-code 4)
    (throw (new RuntimeException "Java source file has to be at least 4 lines long"))
    (str (class-header classname) method-header (lines-of-java (- lines-of-code 4)) footer)))

(defn create-java-file
  [directory-name class-name lines-of-code]
  "Creates a java file with the given number of lines of code. Note that due to class and
   method declarations 4 is the minimum length"
  (let [new-java-file (file-str directory-name File/separatorChar class-name)]
    (do
      (spit new-java-file (java-class class-name lines-of-code))
      new-java-file)))

(deftest test-can-create-fake-files
  (let [java-file (.getAbsolutePath (create-java-file (make-temp-dir) "Bob" 5))]
    (expect (slurp java-file) => "public static class Bob {\npublic static void main(String[] args) {\nSystem.currentTimeMillis();\n}\n}\n")))


(run-tests 'com.thoughtworks.codelapse.fakecode)