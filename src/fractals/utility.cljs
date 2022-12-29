(ns fractals.utility)

(def canvas-padding-px 20)

;; equilateral triangle height
(defn eth [len] (/ (* (Math/sqrt 3) len) 2))

(defn get-centered-equilateral-triangle-canvas-positioning
  [canvas-width canvas-height]
  (let [;; calculate the positioning of the triangle. And by "triangle," I mean
        ;; the resulting triangle-ish Sierpinski curve.
        canvas-width-padded (- canvas-width (* 2 canvas-padding-px))
        canvas-height-padded (- canvas-height (* 2 canvas-padding-px))

        ;; get the height of the resulting triangle if it was based on the
        ;; canvas width/height
        canvas-width-eth (eth canvas-width-padded)

        ;; decrement the canvas-width-padded (or canvas-height-padded, in the
        ;; edge case where it is taller) and keep checking the eth until you
        ;; find the max length (which is the width, since the equilateral
        ;; triangle's "flat" edge is on the bottom) whose height is smaller than
        ;; canvas-height-padded
        max-length-whose-eth-fits-canvas-height-padded
        (->> (iterate dec (max canvas-height-padded canvas-width-padded))
             (drop-while #(> (eth %) canvas-height-padded))
             first)

        ;; if we drew the triangle based on the canvas width, would it be taller
        ;; than the canvas?...
        is-triangle-taller-than-canvas-height (> canvas-width-eth canvas-height-padded)
        ;; ...and then determine what the triangle size will be based on
        triangle-length (if is-triangle-taller-than-canvas-height max-length-whose-eth-fits-canvas-height-padded canvas-width-padded)

        ;; from there, figure out the [bottom left] starting x/y position in
        ;; order to center the triangle in the canvas
        starting-x (if is-triangle-taller-than-canvas-height
                     (+ (- (/ canvas-width-padded 2) (/ max-length-whose-eth-fits-canvas-height-padded 2))
                        canvas-padding-px)
                     canvas-padding-px)
        starting-y (if is-triangle-taller-than-canvas-height
                     (+ canvas-height-padded canvas-padding-px)
                     (- canvas-height-padded (/ (- canvas-height-padded canvas-width-eth) 2)))]
    [starting-x starting-y triangle-length]))

(defn snowflake-height [width] (* (/ (eth width) 3) 4))

(defn get-centered-snowflake-canvas-positioning
  "'Snowflake' is specifically an equilateral triangle with the flat side on the
  bottom plus another 1/3 (of itself) height."
  [canvas-width canvas-height]
  (let [;; calculate the positioning of the triangle. And by "triangle," I mean
        ;; the resulting triangle-ish Sierpinski curve.
        canvas-width-padded (- canvas-width (* 2 canvas-padding-px))
        canvas-height-padded (- canvas-height (* 2 canvas-padding-px))

        ;; get the height of the resulting triangle if it was based on the
        ;; canvas width/height
        canvas-width-snowflake-height (snowflake-height canvas-width-padded)

        ;; decrement the canvas-width-padded (or canvas-height-padded, in the
        ;; edge case where it is taller) and keep checking the snowflake-height until you
        ;; find the max length (which is the width, since the equilateral
        ;; triangle's "flat" edge is on the bottom) whose height is smaller than
        ;; canvas-height-padded
        max-length-whose-snowflake-height-fits-canvas-height-padded
        (->> (iterate dec (max canvas-height-padded canvas-width-padded))
             (drop-while #(> (snowflake-height %) canvas-height-padded))
             first)

        ;; if we drew the triangle based on the canvas width, would it be taller
        ;; than the canvas?...
        is-triangle-taller-than-canvas-height (> canvas-width-snowflake-height canvas-height-padded)

        eth-height (if is-triangle-taller-than-canvas-height
                     (eth max-length-whose-snowflake-height-fits-canvas-height-padded)
                     (eth canvas-width-padded))

        ;; ...and then determine what the triangle size will be based on
        ;; triangle-length (if is-triangle-taller-than-canvas-height max-length-whose-snowflake-height-fits-canvas-height-padded canvas-width-padded)
        triangle-length (if is-triangle-taller-than-canvas-height
                          max-length-whose-snowflake-height-fits-canvas-height-padded
                          ;; (eth max-length-whose-snowflake-height-fits-canvas-height-padded)
                          ;; eth-height
                          canvas-width-padded)

        ;; from there, figure out the [bottom left] starting x/y position in
        ;; order to center the triangle in the canvas
        starting-x (if is-triangle-taller-than-canvas-height
                     (+ (- (/ canvas-width-padded 2) (/ max-length-whose-snowflake-height-fits-canvas-height-padded 2))
                        canvas-padding-px)
                     canvas-padding-px)
        starting-y (if is-triangle-taller-than-canvas-height
                     (+ (/ (- canvas-height (snowflake-height max-length-whose-snowflake-height-fits-canvas-height-padded)) 2) eth-height)
                     (+ (/ (- canvas-height (snowflake-height canvas-width-padded)) 2) (eth canvas-width-padded)))]
    [starting-x starting-y triangle-length]))

(defn get-centered-square-canvas-positioning
  [canvas-width canvas-height canvas-inner-square-size-fn inner-square-padding-fn]
  (let [;; context (.getContext canvas "2d")
        ;; calculate the positioning of the triangle. And by "triangle," I mean
        ;; the resulting triangle-ish Sierpinski curve.
        ;; canvas-padding-px 20
        canvas-width-padded (- canvas-width (* 2 canvas-padding-px))
        canvas-height-padded (- canvas-height (* 2 canvas-padding-px))

        short-edge (if (< canvas-width-padded canvas-height-padded) :width :height)
        short (if (= short-edge :width) canvas-width-padded canvas-height-padded)
        long (if (= short-edge :width) canvas-height-padded canvas-width-padded)

        ;; Since the quadratic koch island goes outside the bounds of the
        ;; original square on each iteration, it needs some extra padding. This
        ;; will give the original square (the drawing of the axiom before any
        ;; rewrites) enough padding to accommodate the iterations.
        canvas-inner-square-size (canvas-inner-square-size-fn short)
        inner-square-padding (inner-square-padding-fn canvas-inner-square-size)

        starting-x (if (= short-edge :height) (+ (/ (- long short) 2) inner-square-padding canvas-padding-px)
                       (+ canvas-padding-px inner-square-padding))
        starting-y (if (= short-edge :width) (- (+ (/ (- long short) 2)
                                                   canvas-width-padded
                                                   canvas-padding-px)
                                                inner-square-padding)
                       (- (+ canvas-height-padded canvas-padding-px) inner-square-padding))]
    [starting-x starting-y canvas-inner-square-size]))
