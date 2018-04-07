(ns realworld.components.app
  (:require [om.next :as om :refer [defui]]
            [sablono.core :as html :refer-macros [html]]         
            [compassus.core :refer [set-route!]]
            [realworld.core :refer [app]]
            [realworld.components.home :refer [Home]]
            [realworld.components.login :refer [Login]]
            [realworld.components.profile :refer [Profile]]
            [realworld.components.settings :refer [Settings]]
            [realworld.components.article-form :refer [ArticleForm]]))

(def routes
  {:home Home
   :login Login
   :profile Profile
   :settings Settings
   :article/create ArticleForm})

(defui Nav
  Object
  (render [this]
    (html [:nav.navbar.navbar-light
            [:div.container
              [:a.navbar-brand {:href "index.html"} "conduit"]
              [:ul.nav.navbar-nav.pull-xs-right
                [:li.nav-item
                  [:a.nav-link.active
                    {:on-click #(set-route! app :home)}
                    "Home"]]
                [:li.nav-item
                  [:a.nav-link.active
                    {:on-click #(set-route! app :article/create)}
                    [:i.ion-compose]
                    " New Post"]]
                [:li.nav-item
                  [:a.nav-link.active
                    {:on-click #(set-route! app :settings)}
                    [:i.ion-gear-a]
                    " Settings"]]
                [:li.nav-item
                  [:a.nav-link.active
                    {:on-click #(set-route! app :login)}
                    "Sign Up"]]]]])))
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

(defui Wrapper
  Object
  (render [this]
    (let [{:keys [factory props]} (om/props this)]
      (html [:div (nav)
                  (factory props)
                  (footer)]))))
