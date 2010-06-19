(ns com.thoughtworks.codelapse.git
  (:use com.thoughtworks.codelapse.utils)
  (:import
    [com.thoughtworks.codelapse Executor]))

;'git --git-dir=' + self.git_dir + ' log --format=format:"%H" -1'

(defn current-head
  [git-executor]
  (git-executor (str "log --format=format:'%H' -1")))

(defn list-all-commits
  [git-executor]
  (map (partial split-on "\t") (split-lines (git-executor (str "--no-pager log --format=format:'%H\t%ai' --date=iso")))))
