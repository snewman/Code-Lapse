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

(defn git-executable
  "Constructs a partial function capable of running git commands (using the command-line process)
  using the given executable, working directory, and git location.
  e.g. (quotes swapped):
  user=> (def git (git-executable '/opt/local/bin/git' '/Users/admin/Development/codelapse/.git' '/Users/admin/Development/codelapse'))
  #'user/git
  user=> (git 'log')"
  [git-executable git-directory working-directory]
  (partial execute git-executable (str "--git-dir=" git-directory) (str "--work-tree=" working-directory)))