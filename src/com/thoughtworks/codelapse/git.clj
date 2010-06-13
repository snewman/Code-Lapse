(ns com.thoughtworks.codelapse.git
  (:import
    [org.eclipse.jgit.lib Repository]))

(defn list-repo
  [location]
  (doto (new Repository location) .getRepositoryState ))
