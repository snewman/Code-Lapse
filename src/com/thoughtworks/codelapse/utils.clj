(ns com.thoughtworks.codelapse.utils
  (:use clojure.contrib.duck-streams)
  (:import
    (java.io IOException ByteArrayOutputStream)
    (java.lang RuntimeException)
    (org.apache.commons.exec CommandLine DefaultExecutor ExecuteWatchdog PumpStreamHandler)))

(defn split [str delimiter]
  (seq (.split str delimiter)))

(defn split-on [delimiter str]
  "Like a normal split, but with the delimiter first to allow for partial application"
  (seq (.split str delimiter)))

(defn split-lines [str]
  (seq (.split #"\r?\n" str)))

(defn execute
  "Executes a command-line program, returning stdout if a zero return code, else the
  error out. Takes a list of strings which represent the command & arguments"
  [& args]
  (try
    (let [process (.exec (Runtime/getRuntime) (reduce str (interleave args (iterate str " "))))]
      (if (= 0 (.waitFor  process))
          (do
            (println "reading")
            (read-lines (.getInputStream process)))
          (read-lines (.getErrorStream process))))
    (catch IOException ioe
      (throw (new RuntimeException (str "Cannot run" args) ioe)))))

(defn alternative-execute
  "Executes a command-line program, returning stdout if a zero return code, else the
  error out. Takes a list of strings which represent the command & arguments"
  [& args]
  (let [output-stream (new ByteArrayOutputStream)
        error-stream (new ByteArrayOutputStream)
        stream-handler (new PumpStreamHandler output-stream error-stream)
        executor (doto
                  (new DefaultExecutor)
                  (.setExitValue 0)
                  (.setStreamHandler stream-handler)
                  (.setWatchdog (new ExecuteWatchdog 20000)))]
     (if (= 0 (.execute executor (CommandLine/parse (apply str (interpose " " args)))))
       (.toString output-stream)
       (.toString error-stream))))

