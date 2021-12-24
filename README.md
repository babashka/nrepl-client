# babashka.nrepl-client

## Status

Still in development, anything can change at this point.

## Usage

``` clojure
(require '[babashka.nrepl-client :as nrepl])
(nrepl/eval-expr {:port 1667 :expr "(+ 1 2 3)"})
;; => {:vals ["6"]}
```

## License

Copyright © 2021 Michiel Borkent

Distributed under the EPL License. See LICENSE.
