(ns com.thoughtworks.codelapse.git
  (:use com.thoughtworks.codelapse.utils midje.semi-sweet clojure.test))

(defn current-head
  [git-executor]
  (git-executor "log --format=format:'%H' -1"))

(defn list-all-commits
  [git-executor]
  (map (partial split-on "\t") (split-lines (git-executor (str "--no-pager log --format=format:'%H\t%ai' --date=iso")))))

(defn hard-reset
  [git-executor commit-hash]
  (git-executor (str "reset --hard " commit-hash)))

(defn git-executable
  "Constructs a partial function capable of running git commands (using the command-line process)
  using the given executable, working directory, and git location.
  e.g. (quotes swapped):
  user=> (def git (git-executable '/opt/local/bin/git' '/Users/admin/Development/codelapse/.git' '/Users/admin/Development/codelapse'))
  #'user/git
  user=> (git 'log')"
  [git-executable git-directory working-directory]
  (partial execute git-executable (str "--git-dir=" git-directory) (str "--work-tree=" working-directory)))

(deftest should-get-current-head
  (let [mock-executor (fn [args] (is (= args "log --format=format:'%H' -1")) "Log Output")]
    (is (=
      (current-head mock-executor)
      "Log Output"))))

(deftest should-list-all-commits
  (let [mock-executor (fn [args] (is (= args "--no-pager log --format=format:'%H\t%ai' --date=iso")) "Commit1\tDate1\nCommit2\tDate2")]
    (is (=
      (list-all-commits mock-executor)
      '(("Commit1" "Date1")("Commit2" "Date2"))))))

(deftest should-execute-hard-reset
  (let [mock-executor (fn [args] (is (= args "reset --hard somehash")))]
    (is (=
      (hard-reset mock-executor "somehash")
      true))))

(run-tests 'com.thoughtworks.codelapse.git)