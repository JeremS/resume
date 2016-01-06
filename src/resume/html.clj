(ns resume.html
  (:require
   [net.cgrand.enlive-html :as en]
   [clojure.string :as s]))


(en/defsnippet info "snippets/info.html" [:section] [] identity)

(defn remove-id [n]
  (update n :attrs dissoc :id))

(en/defsnippet skills "snippets/skills.html" [:#skills] []
  [:#skills] remove-id)


(defn make-dt [dt-string]
    (let [[start end] (-> dt-string
                          s/trim
                          (s/split #"\s-\s")
                          (->> (map #(s/split % #"/"))
                               (map (partial zipmap [:d :label]))))]
      (en/html
       [:div {:class "dt-duration"}
        [:time {:class "dt-start" :datetime (:d start)} (:label start "")]
        (when (:label end ) " - ")
        [:time {:class "dt-end" :datetime (:d end)} (:label end "")]])))



(defn format-header [h]
  (let [content (->> h :content (apply str) s/trim)
        [p-name dt-string](->> content s/split-lines)]
    (en/html
     [:header
      [:h3 {:class "p-name"} p-name]
      (make-dt dt-string)])))



(en/defsnippet experience "snippets/experience.html" [:ol] []
  [:ol :li] (en/do-> (en/add-class "p-experience" "h-event")
                     #(en/at % [:header] format-header)))

(en/defsnippet education "snippets/education.html" [:ol] []
  [:ol :li] (en/do-> (en/add-class "p-education" "h-event")
                     #(en/at % [:header] format-header)))

(en/defsnippet interests "snippets/interests.html" [:#interests] []
  [:#interests] remove-id)



(defn set-tag [new-tag]
  (fn [node]
    (assoc node :tag new-tag)))

(defn morph-tag [t & classes]
  (en/do-> (set-tag t)
           (apply en/add-class classes)))

(defn info-children [] (-> (info) first :content))
(defn info-classes [] (-> (info) first :attrs :class))

(en/defsnippet resume "index.html" [:html] []
  [:#info] (en/do->
             (en/add-class (info-classes))
             (en/append (info-children)))

               [:#skills] (en/append (skills))

  [:#experience] (en/append (experience))

  [:#education] (en/append (education))

  [:#interests] (en/append (interests))

  [:org] (morph-tag :a "h-card" "u-url" "p-org")
  [:location] (morph-tag :span "p-location")
  [:description] (morph-tag :div "p-description")
  [:skill] (morph-tag :a "p-skill"))



(defn make-resume []
  (spit "out/resume.html" (apply str (resume))))


;(make-resume)
