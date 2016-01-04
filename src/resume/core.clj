(ns resume.core
  (:require
   [resume.css :as style]
   [resume.html :as html]
   [cljss.core :as cljss]
   [net.cgrand.enlive-html :as en]
   [clojure.string :as s]
   [clojure.java.io :as io]))


(def reset-css-path "./resources/css/html-doctor.css")

(def dest-css "out/css/style.css")
(def dest-reset "out/css/html-doctor.css")
(def dest-html-dev "out/resume.html")
(def dest-html "out/resumec.html")

(defn reset-css []
  (slurp reset-css-path))



(defn compile-style []
  (cljss/css style/all-rules))



(defn append-stylesheet [path]
  (en/append
    (en/html
      [:link {:rel "stylesheet"
              :type "text/css"
              :href path}])))

(defn add-stylesheet [resume path & paths]
  (en/at resume
         [:head] (->> (cons path paths)
                      (map append-stylesheet)
                      (apply en/do->))))

(defn append-script [path]
  (en/append
    (en/html [:script {:src path :type "text/javascript"}])))

(defn add-script [resume path]
  (en/at resume
         [:body] (append-script path)))



(defn resume-dev []
  (-> (html/resume)
      (add-stylesheet "css/html-doctor.css" "css/style.css")
      (add-script "js/main.js")))




(defn resume-with-inline-headers []
  (-> (html/resume)
      (en/at [:head] (en/do->
                       (en/append (en/html [:style (reset-css)]))
                       (en/append (en/html [:style (compile-style)]))))))


(defn compile-snippet [snippet]
  (->> snippet (en/emit*) (apply str)))

(defn make-directory [path]
  (let [d (io/as-file path)]
    (when-not (.exists d)
      (.mkdir d))))

(defn make-out-directories []
  (make-directory "out")
  (make-directory "out/css"))

(defn compile-dev []
  (do
    (make-out-directories)
    (spit dest-html-dev (compile-snippet (resume-dev)))
    (spit dest-css (compile-style))
    (spit dest-reset (reset-css))))



(defn compile-resume []
  (make-out-directories)
  (spit dest-html (compile-snippet (resume-with-inline-headers))))


(comment
  (->  "out"
       (io/as-file )
       (.exists))

  (compile-dev)
  (compile-resume))
