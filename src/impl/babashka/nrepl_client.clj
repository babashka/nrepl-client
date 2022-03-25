(ns impl.babashka.nrepl-client
  (:require [bencode.core :as b]
            [clojure.edn :as edn]))

(defn parse-number
  "Reads a number from a string. Returns nil if not a number."
  [s]
  (when (re-find #"^-?\d+\.?\d*$" s)
    (edn/read-string s)))

(defn bytes->str [x]
  (if (bytes? x) (String. (bytes x))
      (str x)))

(defn read-msg [msg]
  (let [res (zipmap (map keyword (keys msg))
                    (map #(if (bytes? %)
                            (String. (bytes %))
                            %)
                         (vals msg)))
        res (if-let [status (:status res)]
              (assoc res :status (mapv bytes->str status))
              res)
        res (if-let [status (:sessions res)]
              (assoc res :sessions (mapv bytes->str status))
              res)]
    res))

(defn read-reply [in session id]
  (loop []
    (let [msg (read-msg (b/read-bencode in))]
      (if (and (= (:session msg) session)
               (= (:id msg) id))
        msg
        (recur)))))

(defn coerce-long [x]
  (if (string? x) (parse-number x) x))

(def current-id (atom 0))

(defn next-id []
  (str (swap! current-id inc)))

(defn eval-expr
  "Execute :expr in nREPL on given :host (defaults to localhost)
  and :port. Returns map with :vals. Prints any output to *out*."
  [{:keys [host port expr]}]
  (let [s (java.net.Socket. (or host "localhost") (coerce-long port))
        out (.getOutputStream s)
        in (java.io.PushbackInputStream. (.getInputStream s))
        id (next-id)
        _ (b/write-bencode out {"op" "clone" "id" id})
        {session :new-session} (read-msg (b/read-bencode in))
        id (next-id)
        _ (b/write-bencode out {"op" "eval" "code" expr "id" id "session" session})]
    (loop [values []]
      (let [{:keys [status out value]} (read-reply in session id)]
        (when out
          (print out)
          (flush))
        (if (= status ["done"])
          {:vals values}
          (recur (cond-> values value (conj value))))))))
