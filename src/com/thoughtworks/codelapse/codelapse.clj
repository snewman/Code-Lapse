(ns com.thoughtworks.codelapse.codelapse
  (:use com.thoughtworks.codelapse.linecount midje.semi-sweet clojure.test))

(declare cloc)


; Whe run on some (known) directory create an expected output
(deftest analysis-of-single-commit
  (expect))


(defn with-example-code-directory
  [test]
  ()
  (test)
  ()
 )

(use-fixtures :each with-example-code-directory)
