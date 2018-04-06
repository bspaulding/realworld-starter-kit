(ns realworld.components.app
  (:require [om.next :as om :refer [defui]]
            [sablono.core :as html :refer-macros [html]]
            [realworld.components.home :refer [Home home]]
            [realworld.components.login :refer [Login login]]
            [realworld.components.profile :refer [Profile profile]]
            [realworld.components.settings :refer [Settings settings]]
            [realworld.components.article-form :refer [article-form]]
            [realworld.components.article :refer [Article CommentForm article]]))

(defui Nav
  Object
  (render [this]
    (html [:nav.navbar.navbar-light
            [:div.container
              [:a.navbar-brand {:href "index.html"} "conduit"]
              [:ul.nav.navbar-nav.pull-xs-right
                [:li.nav-item
                  [:a.nav-link.active "Home"]]
                [:li.nav-item
                  [:a.nav-link.active
                    [:i.ion-compose " New Post"]]]
                [:li.nav-item
                  [:a.nav-link.active
                    [:i.ion-gear-a " Settings"]]]
                [:li.nav-item
                  [:a.nav-link.active "Sign Up"]]]]])))
(def nav (om/factory Nav))

(defui Footer
  Object
  (render [this]
    (html [:footer
            [:div.container
              [:a.nav-link {:href "/"} "conduit"]
              [:span.attribution
               ["An interactive learning project from "
                 [:a {:href "https://thinkster.io"} "Thinkster"]
                 ". Code & design licensed under MIT."]]]])))
(def footer (om/factory Footer))

(defui App
  static om/IQuery
  (query [this] [:current-user :articles/list])
  Object
  (render [this]
    #_(.log js/console "app: " (om/props this))
    (html [:div (nav)
                (article (into {} (conj {:current-user (:current-user (om/props this))}
                                        (first (:articles/list (om/props this))))))
                (footer)])))

