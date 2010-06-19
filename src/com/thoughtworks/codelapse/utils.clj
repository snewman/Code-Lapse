(ns com.thoughtworks.codelapse.utils)

(defn split [str delimiter]
  (seq (.split str delimiter)))

(defn split-on [delimiter str]
  "Like a normal split, but with the delimiter first to allow for partial application"
  (seq (.split str delimiter)))

(defn split-lines [str]
  (seq (.split #"\r?\n" str)))
