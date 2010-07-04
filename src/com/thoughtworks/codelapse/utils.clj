(ns com.thoughtworks.codelapse.utils
  (:use clojure.contrib.duck-streams)
  (:import
    (java.io IOException ByteArrayOutputStream)
    (java.lang RuntimeException)
    (org.apache.commons.exec CommandLine DefaultExecutor ExecuteWatchdog PumpStreamHandler ExecuteException)))

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

  (let [output-stream (new ByteArrayOutputStream)
        error-stream (new ByteArrayOutputStream)
        stream-handler (new PumpStreamHandler output-stream error-stream)
        executor (doto
                  (new DefaultExecutor)
                  (.setExitValue 0)
                  (.setStreamHandler stream-handler)
                  (.setWatchdog (new ExecuteWatchdog 20000)))]

    (try
     (if (= 0 (.execute executor (CommandLine/parse (apply str (interpose " " args)))))
       (.toString output-stream)
       (.toString error-stream))
      (catch Exception e (throw (new RuntimeException (str "Error: " (.getMessage e) " Output:" (.toString error-stream)) e))))))

