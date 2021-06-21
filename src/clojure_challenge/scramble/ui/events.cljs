(ns clojure-challenge.scramble.ui.events
  (:require
   [re-frame.core :as re-frame]
   [clojure-challenge.scramble.ui.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]))


(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced
  [{:keys [db]} [_ active-panel]]
  {:db (assoc db :active-panel active-panel)})) ;; escape



(re-frame/reg-event-db
 ::set-value
 (fn-traced
  [db [_ p value]]
  (-> db
      (assoc-in p value))))

(re-frame/reg-event-fx
 ::submit-scramble
 (fn-traced
  [{:keys [db]} [_ s1 s2]]
  {:db         db
   :http-xhrio {:method          :get
                :uri             (str  "/scramble")
                :params          {:s1 s1 :s2 s2}
                :headers         {"Accept" "application/json"}
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success      [::scramble-success]
                :on-failure      [::request-failure]}}))

(re-frame/reg-event-fx
 ::scramble-success
 (fn-traced
  [{:keys [db]} [_ {:keys [result] :as response}]]
  (tap> response)
  {:db (assoc-in db [:scramble-form :result] response)
   :dispatch-later [{:ms 5000 :dispatch [::set-value [:scramble-form :result] nil]}]}))

(re-frame/reg-event-db
 ::request-failure
 (fn-traced
  [db [_ err]]
  (tap> err)
  db))
