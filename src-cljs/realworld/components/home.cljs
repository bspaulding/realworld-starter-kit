(ns realworld.components.home
  (:require [om.next :as om :refer [defui]]
            [realworld.components.article :refer [article-preview]]
            [sablono.core :as html :refer-macros [html]]))


(defui Home
  static om/IQuery
  (query [this] [:articles/list :tags/popular])
  Object
  (render [this]
    (html [:div.home-page
            [:div.banner
              [:div.container
                [:h1.logo-font "conduit"]
                [:p "A place to share your knowledge."]]]
            [:div.container.page
              [:div.row
                [:div.col-md-9
                  [:div.feed-toggle
                    [:ul.nav.nav-pills.outline-active
                      [:li.nav-item
                        [:a.nav-link.disabled {:href ""} "Your feed"]]
                      [:li.nav-item
                        [:a.nav-link.active {:href ""} "Global feed"]]]]
                  (map article-preview (:articles/list (om/props this)))
                  [:div.col-md-3
                    [:div.sidebar
                      [:p "Popular Tags"]
                      [:div.tag-list
                        (map (fn [tag] [:a.tag-pill.tag-default tag])
                             (:tags/popular (om/props this)))]]]]]]])))
(def home (om/factory Home))
