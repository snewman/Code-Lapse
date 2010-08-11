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

(defn clone
  [path-to-git repo-path dir-for-clone]
  "Can't use a git-executable here - work-tree and git-dir don't exist.
  Perhaps clone should return a git-executable?"
  (execute path-to-git (str "clone " repo-path " " dir-for-clone))
  (git-executable path-to-git (str dir-for-clone "/.git") dir-for-clone))

(deftest should-get-current-head
  (let [mock-executor (fn [args] (is (= args "log --format=format:'%H' -1")) "Log Output")]
    (expect (current-head mock-executor) => "Log Output")))

(deftest should-list-all-commits
  (let [mock-executor (fn [args] (is (= args "--no-pager log --format=format:'%H\t%ai' --date=iso")) "Commit1\tDate1\nCommit2\tDate2")]
    (expect (list-all-commits mock-executor) => '(("Commit1" "Date1")("Commit2" "Date2")))))

(deftest should-execute-hard-reset
  (let [mock-executor (fn [args] (is (= args "reset --hard somehash")))]
    (expect (hard-reset mock-executor "somehash") => true)))
;
;(deftest should-clone
;  (let [mock-executor (fn [args] (is (= args "clone git://somerepo.git /dir/for/repo")))]
;    (expect (clone mock-executor "git://somerepo.git" "/dir/for/repo") => true)))

;(run-tests 'com.thoughtworks.codelapse.git)