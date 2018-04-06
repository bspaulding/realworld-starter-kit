(ns realworld.core
  (:require [om.next :as om :refer [defui]]
            [om.dom :as dom]
            [realworld.components.app :refer [App]]))

(def state
  {:articles/list
    [{:article/title "How to build webapps that scale"
      :article/description "This is the description for the post."
      :article/body "Web development technologies have evolved at an incredible clip over the past few years.\n## Introducing RealWorld\nWeb development technologies have evolved at an incredible clip over the past few years."
      :article/created-at (goog.date.Date. 2018 0 20)
      :article/favorites-count 29
      :article/author {:user/name "Eric Simons"
                       :user/image "http://i.imgur.com/Qr71crq.jpg"
                       :user/followers-count 10}
      :article/tags []
      :article/comments [{:comment/body "With supporting text below as a natural lead-in to additional content."
                          :comment/created-at (goog.date.Date. 2017 11 29)
                          :comment/author {:user/name "Jacob Schmidt"
                                           :user/image "http://i.imgur.com/Qr71crq.jpg"}}]}
     {:article/title "The song you won't ever stop singing. No matter how hard you try."
      :article/description "This is the description for the post."
      :article/created-at (goog.date.Date. 2018 0 20)
      :article/favorites-count 32
      :article/author {:user/name "Albert Pai"
                       :user/image "http://i.imgur.com/N4VcUeJ.jpg"
                       :user/followers 15}
      :article/tags ["Music" "Song"]}]
   :tags/popular ["programming" "javascript" "react" "node" "graphql"]
   :current-user {:user/image "http://i.imgur.com/Qr71crq.jpg"}})

(defn read [{:keys [state] :as env} key params]
  (let [st @state
        value (cond
                (and (map? key) (contains? st (ffirst key))) {:value {(ffirst key) (get st (ffirst key))}}
                (contains? st key) {:value (get st key)}
                :else {:value :not-found})]
    (.log js/console "read: " (str key) value)
    value))

(def reconciler (om/reconciler {:state (atom state)
                                :parser (om/parser {:read read})}))
(om/add-root!
  reconciler
  App
  (.querySelector js/document "#app"))
