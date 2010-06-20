(ns com.thoughtworks.codelapse.utils
  (:use clojure.contrib.duck-streams))

(defn split [str delimiter]
  (seq (.split str delimiter)))

(defn split-on [delimiter str]
  "Like a normal split, but with the delimiter first to allow for partial application"
  (seq (.split str delimiter)))

(defn split-lines [str]
  (seq (.split #"\r?\n" str)))

(defn execute
  "Executes a command-line program, returning stdout if a zero return code, else the
  error out. Takes a list of strings which represent the command & arguments"
  [& args]
  (let [process (.exec (Runtime/getRuntime) (space-out args))]
    (if (= 0 (.waitFor  process))
        (read-lines (.getInputStream process))
        (read-lines (.getErrorStream process)))))

(defn space-out
  "Takes multiple strings, returning a single string with spaces between each one"
  [& strings]
  (reduce str (interleave strings) (iterate str " ")))
