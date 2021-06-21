(ns clojure-challenge.scramble.ui.subs
  (:require
   [re-frame.core :as re-frame]
   [clojure.string :refer [blank?]]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::get-value
 (fn [db [_ path]]
   (get-in db path)))

(re-frame/reg-sub
 ::scramble-submit-disabled?
 :<- [::get-value [:scramble-form :s1]]
 :<- [::get-value [:scramble-form :s2]]
 (fn [[s1 s2] _]
   (or (blank? s1)
       (blank? s2))))
