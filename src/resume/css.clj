(ns resume.css
  (:refer-clojure :exclude [+ - * /])
  (:require
   [cljss.core :as cljss]
   [cljss.units.length :as len]
   [cljss.grid :as grid]
   [cljss.units.colors :as color :refer [rgba]]
   [cljss.parse]
   [clojure.algo.generic.arithmetic :as gen :refer [+ - * /]]))

(defmethod cljss.parse/consume-properties clojure.lang.IPersistentSet [[fst scd & rst] node]
  (let [new-props (zipmap fst (repeat scd))]
    (cljss.parse/consume-properties (cons new-props rst) node)))


(def base-font-size (len/rem 1))

(def h1-size (len/rem 2))
(def h2-size (len/rem 1.1))
(def h3-size (len/rem 1.2))




(def font-headings "\"Helvetica Neue\", Helvetica, Arial, sans-serif")
(def font-text "Georgia, \"Times New Roman\", Times, serif")

(cljss/defrules typo
  (cljss/css-comment "----- Typography -------------")


  [:body
     :font-size base-font-size
     :font-family font-text]

  [#{:h1 :h2 :h3 :h4 :h5} :font-family font-headings]

  (map #(vector %1 :font-size %2)
        [:h1 :h2 :h3]
        [h1-size h2-size h3-size])

  [[:#info :h1 :.p-name]
     :font-size h3-size]

  [[#{:#experience :#education} :header :+ :div :a]
   :font-size h2-size]

  [[:#skills :ul :li #{:h3 [:ul :li]}]
     :text-align :center]

  [#{:.p-description
     [:#interests :ul :li :ul :li]}
     :text-align :justify])


(def g (grid/make-grid len/px 30 10))
(def column-number 24)


(defn column [grid-spec span]
  (-> (grid/semantic-column grid-spec span)
      (dissoc :float :display)))

(defn fraction [of mult div]
  (-> of (* mult) (/ div)))


(def left-pane-span (fraction column-number 2 8))
(def main-pane-span (fraction column-number 6 8))

(def break-base base-font-size)
(def half-break (/ break-base 2.0))

(defn break [break-height]
  (list :margin-bottom break-height))

(def left-pane
  #{[:#info :h1 :.p-name]
    [:#info :figure]})

(def main-pane
  #{:#skills :#experience :#education :#interests :hr})

(def main-title [:#info :h1 :.p-summary])


(defn even-odd-children-style [e o]
  (list
    [(cljss/nth-child cljss/& :even) e]
    [(cljss/nth-child cljss/& :odd)  o]))


(cljss/defrules layout
  (cljss/css-comment "----- Layout -------------")
  [:body
   :width (grid/general-width g column-number)
   :margin-left :auto
   :margin-right :auto]

  [main-title
     (column g column-number)
     :display :block
     :margin-top base-font-size
     :margin-bottom base-font-size]

  [left-pane

     :float :left
     :clear :left
     :position :relative
     :top (* 3 base-font-size)
     (column g left-pane-span)]

  [main-pane
   :float :right
   (column g main-pane-span)

   :margin-bottom base-font-size]

  [[:#info :figure :address]
     :margin-top half-break
     :display :block
     (break half-break)]

  [[:#info :figure "div:not(:last-child)"]
   (break half-break)]

  [:h2
     (column g main-pane-span)
     grid/alpha
     grid/omega
     :text-align :center
     (break half-break)]

  [[:#skills :> :ul]
     :display :flex
     :flex-wrap :wrap

   [[:> :li]
      (break half-break)
      (column g (fraction main-pane-span 1 2))
      (even-odd-children-style grid/omega grid/alpha )

      [:ul
         [:li
            (column g (fraction main-pane-span 1 2))
            grid/alpha
            grid/omega]]]]

  [#{:#experience :#education}

     [[:> :ol :> :li]
        (break half-break)]

     [:header
        :display :flex
        :justify-content :space-between
      (break half-break)]

     [[:header :+ :div]
        :position :relative
        :height (* 3 base-font-size)
        (column g left-pane-span)
        (grid/pull g left-pane-span)
        :bottom (+ base-font-size half-break)


        [:.p-location
           :display :block
           :position :relative
           :top half-break]
        [[:+ :div]
           :position :relative
           :bottom (* 3 base-font-size)]]]

  [[:#interests :> :ul :li :ul]
   :display :block
   (break half-break)
   ]
  )

; color palette from https://color.adobe.com/fr/1920-Leyendecker-color-theme-2522272/
(def beige  (rgba "#ECDFBD"))
(def blue   (rgba "#20457C"))
(def brown  (rgba "#3B3A35"))
(def orange (rgba "#FB6648"))
(def violet (rgba "#5E3448"))

(cljss/defrules embelishings
  (cljss/css-comment "----- colors -------------")



  [:body
   :background-color (color/lighten beige 5)
   :color brown]

  [#{:a (cljss/visited :a)}
     :color blue
     :text-decoration :none]

  [#{:h1 :h2 :h3}
   :color violet]

  [:hr
   :color orange
   :background-color orange]

  )


(cljss/defrules all-rules
  typo
  embelishings
  layout)


