(ns com.thoughtworks.codelapse.git-tests
  (:use com.thoughtworks.codelapse.git clojure.test))


(deftest should-get-current-head
  (let [mock-executor (fn [args] (is (= args "log --format=format:'%H' -1")) "Log Output")]
    (is (=
      (current-head mock-executor)
      "Log Output"))))

(run-tests 'com.thoughtworks.codelapse.git-tests)