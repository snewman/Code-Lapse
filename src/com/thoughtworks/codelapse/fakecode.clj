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
  (str (class-header classname) method-header (lines-of-java lines-of-code) footer))

(defn create-java-file
  [directory-name class-name lines-of-code]
  "Creates a java file with the given number of lines of code + 4 (to account for header/footer code
  TODO: ACtually do what it says - so fail if we ask for less than 4 lines of code, but take the header & footer
  into account in lines of code terms"
  (let [new-java-file (file-str directory-name File/separatorChar class-name)]
    (do
      (spit new-java-file (java-class class-name lines-of-code))
      new-java-file)))

(defn make-temp-dir []
  "Creates a temporary directory that will be cleaned up when the JVM exits"
  (let [temp-file  (File/createTempFile "temp" (Long/toString (System/currentTimeMillis)))]
    (do
      (delete-file temp-file)
      (if (.mkdir temp-file)
        (.getAbsolutePath temp-file)
        (throw (java.io.IOException (str "Could not create directory " (.getAbsolutePath temp-file))))))))

(deftest test-can-create-fake-files
  (let [java-file (.getAbsolutePath (create-java-file (make-temp-dir) "Bob" 1))]
    (expect (slurp java-file) => "public static class Bob {\npublic static void main(String[] args) {\nSystem.currentTimeMillis();\n}\n}\n")))


(run-tests 'com.thoughtworks.codelapse.fakecode)