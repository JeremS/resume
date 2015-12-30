(ns resume.css
  (:require
   [cljss.core :as cljss]
   [cljss.units.length :as len]
   [cljss.grid :as grid]))


(def base-font-size 1)


(cljss/defrules typo
  (cljss/css-comment "----- Typography -------------")

  [:body :font-size (len/em 1.1)]

  [:h1
       :font-size (len/em 2)
       :text-align :center

       [:.p-summary
            :font-size (len/em 0.666)]]

  [:h2 :font-size (len/em 1.5)]

  [:h3 :font-size (len/em 1)]

  [:p :text-align :justify]

  [[:#info :figure] :text-align :center]

  )


(def g (grid/make-grid len/px 30 10))
(def column-number 24)


(defn column [grid-spec span]
  (-> (grid/semantic-column grid-spec span)
      (dissoc :float :display)))

(defn fraction [of mult div]
  (-> of (* mult) (/ div)))


(def left-margin (fraction column-number 2 8))


(cljss/defrules layout
  (cljss/css-comment "----- Layout -------------")

  [:body
     :width (grid/general-width g column-number)
     :margin-left :auto
     :margin-right :auto]

  [:section
     :margin-bottom (len/em 1)
     grid/container-mixin
     grid/clearfixed]

  [:h2
     (column g column-number)
     :margin-bottom (len/em 0.75)]

  [:#info
     [#{:.p-name :.p-summary :figure }
        (grid/semantic-column g 24)]]

  [[:#skills :> :ul :> :li]
     (column g 8)
     :display :block
     :float :left
     :margin-bottom (len/em 1)]

  [#{:#experience :#education}
   [[:ol :> :li]
      :margin-bottom (len/em 1)

    [:h3
       (column g 8)
       (grid/semantic-push g left-margin)]

    [:.dt-duration
       (grid/semantic-column g (fraction column-number 2 8))
       :position :relative
       :bottom (len/em 1)
     ]

    [[:> :.h-card]
       (grid/semantic-column g 10)
       (grid/semantic-push g 8)
       :bottom (len/em 1)]

    [:.p-location
       (column g (fraction column-number 2 8))
       :display :block
       :position :relative
       :bottom (len/em 1)]

    [:.p-description ;:background-color :purple
       (grid/semantic-column g (fraction column-number 6 8))
       (grid/semantic-push g left-margin)
       :bottom (len/em 2)

       [:ul :list-style-type :circle]
     ]]]

  [[:#interests :> :ul]
     (column g column-number)
     [[:> :li ]
        :margin-bottom (len/em 1)]
   ]
  )


(cljss/defrules colors
  (cljss/css-comment "----- colors -------------")

  [:body :background-color :#EEE]
  [:section :background-color :#FFFFFF]

  )


(cljss/defrules all-rules
  typo
  colors
  layout)





(defn compile-rules []
  (cljss/css all-rules))


(defn make-rules []
  (spit "out/style.css"
        (compile-rules)))

;(make-rules)


