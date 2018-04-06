(ns realworld.components.login
  (:require [om.next :as om :refer [defui]]
            [sablono.core :as html :refer-macros [html]]))

(defui Login
  Object
  (render [this]
    (html [:div.auth-page
            [:div.container.page
              [:div.row
                [:div.col-md-6.offset-md-3.col-xs-12
                  [:h1.text-xs-center "Sign up"]
                  [:p.text-xs-center [:a "Have an account?"]]
                  [:ul.error-messages
                    [:li "That email is already taken"]]
                  [:form
                    [:fieldset.form-group
                      [:input.form-control.form-control-lg
                        {:type "text"
                         :placeholder "Your name"}]]
                    [:fieldset.form-group
                      [:input.form-control.form-control-lg
                        {:type "text"
                         :placeholder "Email"}]]
                    [:fieldset.form-group
                      [:input.form-control.form-control-lg
                        {:type "password"
                         :placeholder "Password"}]]
                    [:button.btn.btn-lg.btn-primary.pull-xs-right "Sign up"]]]]]])))


(def login (om/factory Login))
