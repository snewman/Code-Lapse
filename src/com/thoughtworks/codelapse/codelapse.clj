(ns com.thoughtworks.codelapse.codelapse
  (:use com.thoughtworks.codelapse.git com.thoughtworks.codelapse.utils com.thoughtworks.codelapse.cloc))

(defn cloc-for-head
  [git-repo working-dir commit]
  "Commit is a triple containing a hash and date"
  (do
    (print (str "processing " commit))
    (hard-reset git-repo (first commit))
    (cloc working-dir)))

(defn end-to-end-git []
  "Checks out the given repo to a temp directory, and cycles throgh all commits"
  (let [tmp-dir (make-temp-dir)
        git-exeuctable (git-executable "/usr/local/bin/git" tmp-dir tmp-dir)
        git-repo (clone "/usr/local/bin/git" "git@github.com:snewman/Code-Lapse.git" tmp-dir)]
    (print (map (partial cloc-for-head git-repo tmp-dir) (list-all-commits git-repo)))))

(defn as-columns
  [records]
  (apply (partial conj #{}) (mapcat keys (map second records))))

(defn header-row
  [records]
  (str "Date," (apply str (interpose "," (as-columns records)))))

(defn values-for-record
  [columns values]
  (for [col columns] (get values col "-")))

(defn as-row
  [record columns]
  (let [day (first record)
        values (second record)]
   (str day ","
    (apply str (interpose "," (values-for-record columns values))))))

(defn as-data
  [records]
  (apply str
    (interpose "\n"
      (for [record records]
          (as-row record (as-columns records))))))

(defn as-table
  [records]
    (apply str (header-row records) "\n" (as-data records)))

(end-to-end-git)

