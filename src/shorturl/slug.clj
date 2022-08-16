(ns shorturl.slug)

(def charset "ABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn generate-slug []
  (->> (repeatedly #(rand-nth charset))
	   (take 4)
	   (apply str)))

(comment
  (take 4 (repeatedly #(rand-nth charset)))
  (generate-slug))
