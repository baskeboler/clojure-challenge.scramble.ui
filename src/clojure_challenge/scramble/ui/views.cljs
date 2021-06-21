(ns clojure-challenge.scramble.ui.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com :refer [at]]
   [clojure-challenge.scramble.ui.styles :as styles]
   [clojure-challenge.scramble.ui.config :as config]
   [clojure-challenge.scramble.ui.events :as events]
   [clojure-challenge.scramble.ui.routes :as routes]
   [clojure-challenge.scramble.ui.subs :as subs]))



;; home



(defn home-title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :src   (at)
     :label (str "Hello from " @name ". This is the Home Page." " Git version " config/version)
     :level :level1
     :class (styles/level1)]))



(defn h2-title [label]
  [re-com/title
   :src (at)
   :label label
   :level :level2
   :class (styles/level2)])

(defn form-input-string [value-path]
  [re-com/input-text
   :src (at)
   :model (re-frame/subscribe [::subs/get-value value-path])
   :on-change (fn [v done]
                (re-frame/dispatch-sync [::events/set-value value-path v])
                (done))
   :placeholder "enter a string"])

(defn scramble-form []
  [re-com/v-box
   :src (at)
   :gap "1em"
   :children [[h2-title "scramble"]
              [form-input-string [:scramble-form :s1]]
              [form-input-string [:scramble-form :s2]]
              [re-com/h-box
               :gap "2em"
               :children [[re-com/button
                           :src (at)
                           :label "Scramble!"
                           :disabled? (re-frame/subscribe [::subs/scramble-submit-disabled?])
                           :on-click (fn [] (re-frame/dispatch [::events/submit-scramble
                                                                @(re-frame/subscribe [::subs/get-value [:scramble-form :s1]])
                                                                @(re-frame/subscribe [::subs/get-value [:scramble-form :s2]])]))]

                          (when-let [{:keys [result]} @(re-frame/subscribe [::subs/get-value [:scramble-form :result]])]

                            [re-com/label
                             :src (at)
                             :label (if result "OK" "NOPE")])]]]])


(defn home-panel []
  [re-com/v-box
   :src      (at)
   :gap      "1em"
   :children [[home-title]
              [scramble-form]]])


(defmethod routes/panels :home-panel [] [home-panel])


;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [re-com/v-box
     :src      (at)
     :height   "100%"
     :children [(routes/panels @active-panel)]]))
