(ns drums.core
  (:require [overtone.live :refer :all]))

(def loop1 (sample "/home/cblop/Audio/drum_loops/Beta Monkey Drum Werks V - Alternative Rock/158 BPM Rock Pop Grooves/RC_Groove_01.wav"))

(defn frames [s]
  (:size (buffer-info s)))

(loop1 :start-pos (slice loop1 15))


(:size (buffer-info loop1))
(slice loop1 1)

(def loop2 (load-sample "/home/cblop/Audio/drum_loops/Beta Monkey Drum Werks V - Alternative Rock/120 BPM Rock and Pop Grooves/HH_Groove_04.wav"))

(buffer)

(defn get-frame [s n]
  (int (* n (/ (:size (buffer-info s)) 16)))
  )

(defn frames-to-seconds [f]
  (let [sample-rate (:sample-rate (server-info))]
    (double (/ f sample-rate))))

(frames-to-seconds (get-frame loop2 8))
(get-frame loop2 16)

(def slices
  [[0 4]
   [4 6]
   [7 11]
   [12 16]
   [14 16]])

(type 9.9)

(defsynth slicer [b (buffer (:sample-rate (server-info))) start 0 end 16]
  (let [
        ;; duration (frames-to-seconds (- (get-frame b end) (get-frame b start)))
        duration 1.0
        env (env-gen (envelope [1 0] [duration]))
        ;; start (get-frame b 14)
        pbuf (play-buf 1 b :start-pos 1)
        ]
    (out 0 [pbuf pbuf])))

(defn slice [s n]
  (int (* n (/ (frames s) 16))))

(defn sl [b start end]
  (let [dur (frames-to-seconds (- (get-frame b end) (get-frame b start)))]
    (pl b (slice b start) dur)))

(defsynth pl [b (buffer (:sample-rate (server-info))) start 0 dur 1]
  (let [env (env-gen (envelope [1 0] [dur]))
        pbuf (play-buf 1 b :start-pos start)]
    (out 0 (* env pbuf))))

(defn make-slices [sample ss]
  (map #(apply slicer (cons sample %)) ss))

(make-slices loop2 slices)

()



(pl loop2 (slice loop2 4) (slice loop2 6))
(pl loop2 (slice loop2 0) 2)
(sl loop2 6 9)
(sl loop2 14)

(loop1)

(def mn (metronome 120))

(let [nome (metronome 120)
      slice1 (slice loop2 0)]
  (at (nome 10) (pl loop2 slice1))
  )

(buffer-info loop2)

(defsynth bet []
  (let [env (env-gen (envelope [1 0] [1]))]
    (out 0 (* env (play-buf 1 loop2 :start-pos 0)))
    ;; (out 0 (play-buf 1 loop2 :start-pos 0))
    ;; (out 0 (loop1))
    ))

(bet)

(stop)


(defn make-beat [name duration beats]
  {:name name
   :duration duration
   :beats beats})

(defn play-beat [buf slices])

(slicer loop2 )


(out 0 (play-buf loop2))

(play-buf 1 loop2)

