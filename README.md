# babashka.nrepl-client

## Status

Still in development, anything can change at this point.

## Usage

``` clojure
(require '[babashka.nrepl-client :as nrepl])

(-> (nrepl/eval-expr {:port 53099 :expr "(+ 1 2 3) (+ 1 2 3)"}) (clojure.pprint/pprint))
{:vals ["6" "6"],
 :responses
 [{:id "6",
   :ns "user",
   :session "30b3b0df-2e77-43d2-a9f6-e2fa1430f3df",
   :value "6"}
  {:id "6",
   :ns "user",
   :session "30b3b0df-2e77-43d2-a9f6-e2fa1430f3df",
   :value "6"}
  {:id "6",
   :session "30b3b0df-2e77-43d2-a9f6-e2fa1430f3df",
   :status ["done"]}]}
```

Example in `bb.edn` tasks:

``` clojure
{:deps {babashka/nrepl-client {:git/url "https://github.com/babashka/nrepl-client"
                               :git/sha "19fbef2525e47d80b9278c49a545de58f48ee7cf"}}
 :tasks {nrepl-eval {:requires ([babashka.nrepl-client :as nrepl])
                     :task (let [values (:vals (nrepl/eval-expr
                                                {:port (first *command-line-args*)
                                                 :expr (second *command-line-args*)}))]
                             (run! println values))}}}
```

Then run:

```
$ bb nrepl-eval 1667 "(defn foo [] 3) (+ 1 2 (foo))"
#'user/foo
6
```

## License

Copyright © 2021-2025 Michiel Borkent

Distributed under the EPL License. See LICENSE.
