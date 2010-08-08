(ns com.thoughtworks.codelapse.codelapse
  (:use com.thoughtworks.codelapse.git com.thoughtworks.codelapse.utils com.thoughtworks.codelapse.cloc))

(defn cloc-for-head
  [git-repo working-dir hash]
  (do
    (hard-reset git-repo hash)
    (cloc working-dir)))

(defn end-to-end-git []
  "Checks out the given repo to a temp directory, and cycles throgh all commits"
  (let [tmp-dir (make-temp-dir)
        git-exeuctable (git-executable "/usr/local/bin/git" tmp-dir tmp-dir)
        git-repo (clone "/usr/local/bin/git" "git@github.com:snewman/Code-Lapse.git" tmp-dir)
        ]
    (print (map (partial cloc-for-head git-repo tmp-dir) (map first (list-all-commits git-repo))))))

(end-to-end-git)

