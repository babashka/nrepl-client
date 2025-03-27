(ns babashka.nrepl-client
  {:clj-kondo/config '{:linters {:unused-binding {:exclude-destructured-keys-in-fn-args true}}}}
  (:require [impl.babashka.nrepl-client :as impl]))

(defn eval-expr
  "Execute :expr in nREPL on given :host (defaults to localhost)
  and :port. Returns map with :vals. Prints any output to *out*.

  :vals is a vector with eval results from all the top-level
  forms in the :expr. See the README for an example."
  [{:keys [host port expr] :as opts}]
  (impl/eval-expr opts))
