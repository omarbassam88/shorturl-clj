(ns shorturl.core
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [ring.util.response :as r]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [shorturl.db :as db]
            [shorturl.slug :refer [generate-slug]]
            [muuntaja.core :as m]))

(defn redirect [request]
  (let [slug (get-in request [:path-params :slug])
        url (db/get-url slug)]
    (if url
      (r/redirect url 307)
      (r/not-found "URL NOT FOUND"))))

(defn create-redirect
  [req]
  (let [url (get-in req [:body-params :url])
        slug (generate-slug)]
    (db/insert-redirect! slug url)
    (r/response (str "URL Created "  slug))))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     [":slug" redirect]
     ["api/"
      ["redirect" {:post create-redirect}]]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn start []
  (jetty/run-jetty #'app {:port 3000
                          :join? false}))

(defn -main [& args]
  (start))

(comment
  (def server (start))
  (.stop server))
