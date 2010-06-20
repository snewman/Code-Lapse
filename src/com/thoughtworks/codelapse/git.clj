(ns com.thoughtworks.codelapse.git
  (:use com.thoughtworks.codelapse.utils))

(defn current-head
  [git-executor]
  (git-executor "log --format=format:'%H' -1"))

(defn list-all-commits
  [git-executor]
  (map (partial split-on "\t") (split-lines (git-executor (str "--no-pager log --format=format:'%H\t%ai' --date=iso")))))

(defn hard-reset
  [git-executor commit-hash]
  (git-executor (str "reset --hard " commit-hash)))

