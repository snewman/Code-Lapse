(ns com.thoughtworks.codelapse.codelapse
  (:use
    com.thoughtworks.codelapse.git
    com.thoughtworks.codelapse.utils
    com.thoughtworks.codelapse.cloc
    midje.semi-sweet
    clojure.test
    clojure.contrib.str-utils))

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
  (str "Date," (str-join "," (as-columns records))))

(defn values-for-record
  [columns values]
  (for [col columns] (get values col "-")))

(defn as-row
  [record columns]
  (let [day (first record)
        values (second record)]
   (str day "," (str-join "," (values-for-record columns values)))))

(defn as-data
  [records]
  (str-join "\n"
      (for [record records]
          (as-row record (as-columns records)))))

(defn as-table
  [records]
  "Converts Clojure data in the form:
  ((\"ID1\" {:a 1 :b 2 :c 3})
  (\"ID1\" { :b 2 :c 3}))
  To sparse tablular Data"
    (apply str (header-row records) "\n" (as-data records)))

(deftest should-extract-known-columns-from-records
  (expect (as-columns '(("ID1" {"a" 1 "b" 2}))) => #{"a" "b"})
  (expect (as-columns '(("ID1" {"a" 1 "b" 2}) ("ID2" {"b" 1 "c" 2}))) => #{"a" "b" "c"}))

(deftest should-create-header-row
  (expect (header-row '(("ID" {"a" 1 "b" 2}))) => "Date,a,b"))

(deftest should-create-dense-table-from-records
  (expect (as-table '(("ID1" {"a" 1 "b" 2}))) => "Date,a,b\nID1,1,2")
  (expect (as-table '(("ID1" {"a" 1 "b" 2}) ("ID2" {"a" 3 "b" 5}))) => "Date,a,b\nID1,1,2\nID2,3,5"))

(deftest should-create-sparse-table-from-records
  (expect (as-table '(("ID1" {"b" 1 "c" 2}) ("ID2" {"a" 3 "b" 5}))) => "Date,a,b,c\nID1,-,1,2\nID2,3,5,-"))

(run-tests 'com.thoughtworks.codelapse.codelapse)
; (end-to-end-git)

