(ns com.thoughtworks.codelapse.git
  (:import
    [com.thoughtworks.codelapse Executor]))

;'git --git-dir=' + self.git_dir + ' log --format=format:"%H" -1'

(defn current-head
  [git-executor]
  (git-executor (str "log --format=format:'%H' -1")))
