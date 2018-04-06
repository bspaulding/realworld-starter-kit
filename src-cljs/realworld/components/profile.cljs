(ns realworld.components.profile
  (:require [om.next :as om :refer [defui]]
            [sablono.core :as html :refer-macros [html]]
            [realworld.components.article :refer [article-preview]]))

(defui Profile
  static om/IQuery
  (query [this] [:articles/list])
  Object
  (render [this]
    (html [:div.profile-page
            [:div.user-info
              [:div.container
                [:div.row
                  [:div.col-xs-12.col-md-10.offset-md-1
                    [:img.user-img {:src "http://i.imgur.com/Qr71crq.jpg"}]
                    [:h4 "Eric Simons"]
                    [:p "Cofounder @GoThinkster, lived in Aol's HQ for a few months, kinda looks like Peeta from the Hunger Games"]
                    [:button.btn.btn-sm.btn-outline-secondary.action-btn
                      [:i.ion-plus-round]
                      " Follow Eric Simons"]]]]]
            [:div.container
              [:div.row
                  [:div.col-xs-12.col-md-10.offset-md-1
                    [:div.articles-toggle
                      [:ul.nav.nav-pills.outline-active
                        [:li.nav-item [:a.nav-link.active "My Articles"]]
                        [:li.nav-item [:a.nav-link "Favorited Articles"]]]]
                    (map article-preview (:articles/list (om/props this)))]]]])))

(def profile (om/factory Profile))
