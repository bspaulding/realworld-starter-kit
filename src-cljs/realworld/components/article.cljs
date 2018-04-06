(ns realworld.components.article
  (:require [om.next :as om :refer [defui]]
            [goog.i18n.DateTimeFormat :as dtf]
            [sablono.core :as html :refer-macros [html]]
            [markdown.core :refer [md->html]]))

(def formatter (goog.i18n.DateTimeFormat. "MMMM d"))
(def short-month-format (goog.i18n.DateTimeFormat. "MMM d"))

(defui UserInfo
  Object
  (render [this]
    (html
      [:div.info
        [:a.author {:href ""} (get-in (om/props this) [:article/author :user/name])]
        [:span.date (.format formatter (get-in (om/props this) [:article/created-at]))]])))
(def user-info (om/factory UserInfo))

(defui ArticlePreview
  Object
  (render [this]
    (html
      [:div.article-preview
        [:div.article-meta
          [:a {:href "profile.html"}
            [:img {:src (get-in (om/props this) [:article/author :user/image])}]]
          (user-info (om/props this))
          [:button.btn.btn-outline-primary.btn-sm.pull-xs-right
            [:i.ion-heart]
            (str " " (:article/favorites-count (om/props this)))]]
        [:a.preview-link {:href ""}
          [:h1 (get-in (om/props this) [:article/title])]
          [:p (get-in (om/props this) [:article/description])]
          [:span "Read more..."]
          [:ul.tag-list
            (map (fn [tag] [:li.tag-default.tag-pill.tag-outline tag])
                 (:article/tags (om/props this)))]]])))

(def article-preview (om/factory ArticlePreview))

(defui ArticleMeta
  Object
  (render [this]
    (html
			[:div.article-meta
				[:a {:href ""}
					[:img {:src (get-in (om/props this)
															[:article/author :user/image])}]]
				(user-info (om/props this))
				[:button.btn.btn-sm.btn-outline-secondary
					[:i.ion-plus-round]
					(str " Follow " (get-in (om/props this)
																	[:article/author :user/name]))
					[:span.counter (str " ("
															(get-in (om/props this) [:article/author :user/followers-count])
															")")]]
				"  "
				[:button.btn.btn-sm.btn-outline-primary
					[:i.ion-heart]
					" Favorite Post"
					[:span.counter (str " ("
															(:article/favorites-count (om/props this))
															")")]]])))
(def article-meta (om/factory ArticleMeta))

(defui CommentForm
  static om/IQuery
  (query [this] [{:current-user [:user/image]}])
  Object
  (render [this]
    (html [:form.card.comment-form
            [:div.card-block
              [:textarea.form-control
                {:placeholder "Write a comment..."
                 :rows 3}]]
            [:div.card-footer
              [:img.comment-author-img {:src (get-in (om/props this) [:current-user :user/image])}]
              [:button.btn.btn-sm.btn-primary "Post Comment"]]])))
(def comment-form (om/factory CommentForm))

(defui Comment
  static om/IQuery
  (query [this] [:comment/body
                 :comment/created-at
                 {:comment/author [:user/name :user/image]}])
  Object
  (render [this]
    (let [props (om/props this)]
      (html [:div.card
              [:div.card-block
                [:p.card-text (:comment/body props)]]
              [:div.card-footer
                [:a.comment-author
                  [:img.comment-author-img {:src (get-in props [:comment/author :user/image])}]]
                " "
                [:a.comment-author (get-in props [:comment/author :user/name])]
                [:span.date-posted (.format short-month-format (get-in props [:comment/created-at]))]
                [:span.mod-options
                  [:i.ion-edit]
                  [:i.ion-trash-a]]]]))))
(def article-comment (om/factory Comment))

(defui Article
  static om/IQuery
  (query [this] [:article/title
                 {:article/author [:user/image :user/name :user/followers]}
                 :article/favorites-count
                 {:article/comments (om/get-query Comment)}])
  Object
  (render [this]
    #_(.log js/console "article: " (om/props this))
    (html [:div.article-page
            [:div.banner
              [:div.container
                [:h1 (:article/title (om/props this))]
                (article-meta (om/props this))]]
            [:div.container.page
              [:div.row.article-content
                [:div.col-md-12 {:dangerouslySetInnerHTML {:__html (md->html (:article/body (om/props this)))}}]]
              [:hr]
              (article-meta (om/props this))
              [:div.row
                [:div.col-xs-12.col-md-8.offset-md-2
                  (comment-form (om/props this))
                  (map article-comment (:article/comments (om/props this)))]]]])))
(def article (om/factory Article))
