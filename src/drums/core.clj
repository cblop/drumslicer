(ns drums.core
  (:require [overtone.live :refer :all]))

(defn frames [s]
  (:size (buffer-info s)))

(def loop1 (load-sample "/home/cblop/Audio/drum_loops/Beta Monkey Drum Werks V - Alternative Rock/120 BPM Rock and Pop Grooves/Simple Patterns/HH_Groove_21s.wav"))
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
  (let [env (env-gen (envelope [1 1 0] [dur 0.2] :welch))
        pbuf (play-buf 1 b :start-pos start)]
    (out 0 (* env pbuf))
    ;; (out 0 pbuf)
    ))

(defn make-slices [sample ss]
  (map #(apply parts (cons sample %)) ss))

(def slices
  [[0 4]
   [4 5]
   [7 11]
   [12 16]
   [14 16]])

(def sliced (make-slices loop2 slices))
(def diced (make-slices loop1 [[0 2]
                               [9 16]
                               [4 6]
                               [2 4]
                               [9 12]]))

(apply pl (first sliced))
(apply pl (nth sliced 4))
(apply pl (first diced))
(apply pl (nth diced 3))



(pl loop2 (slice loop2 0) 2)
(sl loop2 0 16)
(sl loop1 0 6)

(let [nome (metronome 100)
      ns (range 0 (count sliced))]
  (doseq [x ns] (at (nome (* x 2)) (apply pl (nth sliced x))))
  )

(buffer-info loop1)

(defn make-beat [bpm length beats]
  {:bpm bpm
   :length length
   :beats beats})

(def verse-beat (make-beat 120 16
                           [[sliced [[0 0] [1 2] [2 4] [3 6] [4 8]]]
                            [diced [[0 1] [1 3] [2 5] [3 7] [3 8]]]
                            ]))

(:bpm verse-beat)
(:beats verse-beat)

(defn play-beat [{:keys [bpm length beats]}]
  (let [nome (metronome bpm)]
    (doseq [x beats y (second x)]
      (at (nome (second y)) (apply pl (nth (first x) (first y))))
  )))

(play-beat verse-beat)

