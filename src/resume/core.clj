(ns resume.core
  (:require
   [resume.css :as style]
   [resume.html :as html]
   [cljss.core :as cljss]
   [net.cgrand.enlive-html :as en]
   [clojure.string :as s]))


(def reset-css-path "./resources/css/html-doctor.css")

(def dest-css "out/style.css")
(def dest-reset "out/html-doctor.css")
(def dest-html-dev "out/resume.html")
(def dest-html "out/resumec.html")

(defn reset-css []
  (slurp reset-css-path))



(defn compile-style []
  (cljss/css style/all-rules))


(defn add-style-sheet [path]
  (en/append
   (en/html
    [:link {:rel "stylesheet"
            :type "text/css"
            :href path}])))

(defn resume-with-dev-headers []
  (en/at (html/resume)
         [:head] (en/do->
                  (add-style-sheet "html-doctor.css")
                  (add-style-sheet "style.css"))))



(defn resume-with-inline-headers []
  (en/at (html/resume)
         [:head] (en/do->
                  (en/append (en/html [:style (reset-css)]))
                  (en/append (en/html [:style (compile-style)])))))


(defn compile-snippet [snippet]
  (->> snippet (en/emit*) (apply str)))


(defn compile-dev []
  (do
    (spit dest-html-dev (compile-snippet (resume-with-dev-headers)))
    (spit dest-css (compile-style))
    (spit dest-reset (reset-css))))



(defn compile-resume []
  (spit dest-html (compile-snippet (resume-with-inline-headers))))

;(compile-dev)
;(compile-resume)
