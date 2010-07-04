(ns com.thoughtworks.codelapse.codelapse
  (:use com.thoughtworks.codelapse.linecount midje.semi-sweet clojure.test clojure.contrib.io)
  (:import
    (java.io File)))

(declare cloc)



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
  [directory name lines-of-code]
  (let [new-java-file (new File directory (str name ".java"))]
  (spit new-java-file (java-class name lines-of-code))))
