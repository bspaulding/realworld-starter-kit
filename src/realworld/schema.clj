(ns realworld.schema
  (:require [datomic.api :as d]))

(def schema
  [{:db/ident :user/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :user/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :user/bio
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/image
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/following
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident :tag
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident :article/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident :article/slug
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :article/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :article/description
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :article/body
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :article/tags
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many}
   {:db/ident :article/author
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :article/comments
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many}
   {:db/ident :article/favorited-by
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many}

   {:db/ident :comment/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident :comment/body
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :comment/author
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :comment/article
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}])

(def db-uri "datomic:dev://localhost:4334/realworld")
(d/delete-database db-uri)
(d/create-database db-uri)
(def conn (d/connect db-uri))
(d/transact conn schema)

(def sample-user
  [{:user/id (d/squuid)
    :user/name "jake"
    :user/bio "I work at statefarm"
    :user/image ""}])
(d/transact conn sample-user)
(def sample-user-2
  [{:user/id (d/squuid)
    :user/name "gecko"
    :user/bio "I work at geico"
    :user/image ""
    :user/following [[:user/id (:id sample-user)]]}])
(d/transact conn sample-user-2)

(defn user-map [[_ username bio id image]]
  {:id id
   :username username
   :bio bio
   :image image})

(defn get-user-by-name-query [uname]
  `[:find ?e ?name ?bio ?id ?image
    :where [?e :user/name ?name]
           [?e :user/name ~uname]
           [?e :user/bio ?bio]
           [?e :user/id ?id]
           [?e :user/image ?image]])

(defn get-user-by-name [uname db]
  (->
    (d/q (get-user-by-name-query uname) db)
    first
    user-map))
(def jake (get-user-by-name "jake" (d/db conn)))
(def gecko (get-user-by-name "gecko" (d/db conn)))

(def sample-article
  [{:article/id (d/squuid)
    :article/slug "how-to-train-your-dragon"
    :article/title "How to train your dragon"
    :article/description "Ever wonder how?"
    :article/author [:user/id (:id jake)]
    :article/body "It takes a Jacobian"}])
(d/transact conn sample-article)
(def sample-article-2
  [{:article/id (d/squuid)
    :article/slug "how-to-train-your-dragon-2"
    :article/title "How to train your dragon 2"
    :article/description "So toothless"
    :article/author [:user/id (:id jake)]
    :article/body "Its a dragon"}])
(d/transact conn sample-article-2)

(defn get-article-by-slug-query [slug]
  `[:find ?e ?id ?slug ?title ?description ?body
          ?uname ?ubio ?uimage
    :where  [?e :article/id ?id]
            [?e :article/slug ?slug]
            [?e :article/slug ~slug]
            [?e :article/title ?title]
            [?e :article/description ?description]
            [?e :article/body ?body]
            [?e :article/author ?u]
            [?u :user/name ?uname]
            [?u :user/bio ?ubio]
            [?u :user/image ?uimage]])
(get-article-by-slug-query "how-to-train-your-dragon")

(defn article-map [[_ id slug title description body uname ubio uimage]]
  {:id id
   :slug slug
   :title title
   :description description
   :body body
   :author {:username uname
            :bio ubio
            :image uimage}})

(defn get-article-by-slug [slug db]
  (-> (d/q (get-article-by-slug-query slug) db)
      first
      article-map))
(get-article-by-slug "how-to-train-your-dragon" (d/db conn))

(defn get-articles-for-author-id [uid db]
  (->>
    (d/q `[:find ?e ?slug ?title ?description ?body
                 ?uname ?ubio ?uimage
           :where [?e :article/slug ?slug]
                  [?e :article/title ?title]
                  [?e :article/description ?description]
                  [?e :article/body ?body]
                  [?e :article/author [:user/id ~uid]]
                  [?e :article/author ?u]
                  [?u :user/name ?uname]
                  [?u :user/bio ?ubio]
                  [?u :user/image ?uimage]]
         db)
    (map article-map)))
(get-articles-for-author-id (:id jake) (d/db conn))

(defn get-all-articles [db]
  (->>
    (d/q '[:find ?e ?slug ?title ?description ?body
                 ?uname ?ubio ?uimage
           :where [?e :article/slug ?slug]
                  [?e :article/title ?title]
                  [?e :article/description ?description]
                  [?e :article/body ?body]
                  [?e :article/author ?u]
                  [?u :user/name ?uname]
                  [?u :user/bio ?ubio]
                  [?u :user/image ?uimage]]
         db)
    (map article-map)))
(get-all-articles (d/db conn))

(def sample-comment
  (let [cid (d/squuid)]
    [{:db/id "cid"
      :comment/id cid
      :comment/body "It takes a Jacobian"
      :comment/author [:user/id (:id jake)]
      :comment/article [:article/slug "how-to-train-your-dragon"]}
     {:db/id [:article/slug "how-to-train-your-dragon"]
      :article/comments ["cid"]}]))
(d/transact conn sample-comment)

(d/q '[:find ?e ?id ?body
             ?uname ?ubio ?uimage
       :where [?e :comment/id ?id]
              [?e :comment/body ?body]
              [?e :comment/author ?u]
              [?e :comment/article [:article/slug "how-to-train-your-dragon"]]
              [?u :user/name ?uname]
              [?u :user/bio ?ubio]
              [?u :user/image ?uimage]]
      (d/db conn))

(d/pull (d/db conn)
        ;; SELECT
        '[:article/slug
          :article/title
          {:article/author [:user/name]}
          {:article/comments [:comment/body {:comment/author [:user/name]}]}]
        ;; WHERE
        [:article/slug "how-to-train-your-dragon"])
(d/pull (d/db conn)
        [:article/title]
        [:article/slug "how-to-train-your-dragon"])
(d/pull (d/db conn)
        '[:user/name {:user/following [:user/name]}]
        [:user/id (:id gecko)])
