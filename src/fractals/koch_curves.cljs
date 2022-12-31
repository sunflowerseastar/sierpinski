(ns fractals.koch-curves
  (:require
   [fractals.l-system :refer [l-system]]))

(def koch-variations
  [{:name "rings"
    :variables #{:F}
    :constants #{:+ :-}
    :start [:F :- :F :- :F :- :F]
    :rules {:F [:F :F :- :F :- :F :- :F :- :F :- :F :+ :F]}
    :actions {:F :forward :+ :left :- :right}
    :delta 90
    :max-iterations 4}
   {:name "box"
    :variables #{:F}
    :constants #{:+ :-}
    :start [:F :- :F :- :F :- :F]
    :rules {:F [:F :F :- :F :- :F :- :F :- :F :F]}
    :actions {:F :forward :+ :left :- :right}
    :delta 90
    :initial-num-iterations 4
    :max-iterations 5}
   {:name "crystal"
    :variables #{:F}
    :constants #{:+ :-}
    :start [:F :- :F :- :F :- :F]
    :rules {:F [:F :F :- :F :- :- :F :- :F]}
    :actions {:F :forward :+ :left :- :right}
    :delta 90
    :initial-num-iterations 4
    :max-iterations 5}])

(defn koch-curves [window-width]
  (l-system window-width koch-variations))
