(ns com.thoughtworks.codelapse.git-tests
  (:use com.thoughtworks.codelapse.git clojure.test))

(deftest should-get-current-head
  (let [mock-executor (fn [args] (is (= args "log --format=format:'%H' -1")) "Log Output")]
    (is (=
      (current-head mock-executor)
      "Log Output"))))

;--no-pager log --format=format:"%H || %ai || %s%n" --date=iso

(deftest should-list-all-commits
  (let [mock-executor (fn [args] (is (= args "--no-pager log --format=format:'%H\t%ai' --date=iso")) "Commit1\tDate1\nCommit2\tDate2")]
    (is (=
      (list-all-commits mock-executor)
      '(("Commit1" "Date1")("Commit2" "Date2"))))))

(run-tests 'com.thoughtworks.codelapse.git-tests)