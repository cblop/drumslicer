(ns drums.core
  (:require [overtone.live :refer :all]))

(defn frames [s]
  (:size (buffer-info s)))

(def loop2 (load-sample "/home/cblop/Audio/drum_loops/Beta Monkey Drum Werks V - Alternative Rock/120 BPM Rock and Pop Grooves/HH_Groove_04.wav"))

(defn get-frame [s n]
  (int (* n (/ (:size (buffer-info s)) 16)))
  )

(defn frames-to-seconds [f]
  (let [sample-rate (:sample-rate (server-info))]
    (double (/ f sample-rate))))

(defn slice [s n]
  (int (* n (/ (frames s) 16))))

(defn sl [b start end]
  (let [dur (frames-to-seconds (- (get-frame b end) (get-frame b start)))]
    (pl b (slice b start) dur)))

(defn parts [b start end]
  (let [dur (frames-to-seconds (- (get-frame b end) (get-frame b start)))]
    [b (slice b start) dur]))

(defsynth pl [b (buffer (:sample-rate (server-info))) start 0 dur 1]
  (let [env (env-gen (envelope [1 0] [dur]))
        pbuf (play-buf 1 b :start-pos start)]
    (out 0 (* env pbuf))))

(defn make-slices [sample ss]
  (map #(apply parts (cons sample %)) ss))

(def slices
  [[0 4]
   [4 5]
   [7 11]
   [12 16]
   [14 16]])

(def sliced (make-slices loop2 slices))

(apply pl (first sliced))
(apply pl (nth sliced 4))


(pl loop2 (slice loop2 0) 2)
(sl loop2 6 9)

(def mn (metronome 120))

(let [nome (metronome 400)
      ns (range 0 (count sliced))]
  (doseq [x ns] (at (nome (* x 5)) (apply pl (nth sliced x))))
  )

(buffer-info loop2)

(defn make-beat [name duration beats]
  {:name name
   :duration duration
   :beats beats})

(defn play-beat [buf slices])

