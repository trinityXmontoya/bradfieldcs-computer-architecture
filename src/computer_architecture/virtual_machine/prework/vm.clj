(ns computer_architecture.virtual_machine.prework.vm)

;-------------
;MAIN MEMORY
;-------------
(def main-memory (atom nil))

(defn get-main-memory
  [key]
  (@main-memory key))

(defn set-main-memory-output
  [val]
  (swap! main-memory assoc :output val))

;-------------
;REGISTERS
;-------------
(def program-counter-addr 0x00)
(def register-1-addr 0x01)
(def register-2-addr 0x02)

(def registers
  (atom
    { program-counter-addr 0
      register-1-addr nil
      register-2-addr nil }))

(defn set-register
  [addr val]
  (println "setting" addr val)
  (swap! registers assoc addr val))

(defn get-register
  [addr]
  (println "getting" addr (@registers addr))
  (@registers addr))

;-------------
;INSTRUCTIONS
;-------------
(def instruction-set
  { 0x01 :load_word
    0x02 :store_word
    0x03 :add
    0x04 :sub
    0xff :halt })

(def instruction-len 14)
(def instruction-start-index 0)
(def instruction-end-index (+ instruction-start-index instruction-len))

(def output-len 2)
(def output-start-index instruction-end-index)
(def output-end-index (+ output-start-index output-len))

(def input-1-len 2)
(def input-1-start-index output-end-index)
(def input-1-end-index (+ input-1-start-index input-1-len))

(def input-2-len 2)
(def input-2-start-index input-1-end-index)
(def input-2-end-index (+ input-2-start-index input-2-len))

(defn parse-memory
  [memory]
  {:instructions (subvec memory instruction-start-index instruction-end-index)
   :output       (subvec memory output-start-index output-end-index)
   :input-1      (subvec memory input-1-start-index input-1-end-index)
   :input-2      (subvec memory input-2-start-index input-2-end-index)})

(declare choose-and-convert-input)
(defn load-word
  "Load value at given address into register"
  [reg addr]
  (let [input (choose-and-convert-input addr)]
    (set-register reg input)))

(declare convert-output)
(defn store-word
  "Store the value in register at the given address"
  [reg addr]
  (let [output (convert-output (get-register reg))]
    (set-main-memory-output output)))

(defn add
  "Set reg1 = reg1 + reg2"
  [reg1 reg2]
  (set-register reg1 (+ (get-register reg1) (get-register reg2))))

(defn sub
  "Set reg1 = reg1 - reg2"
  [reg1 reg2]
  (set-register reg1 (- (get-register reg1) (get-register reg2))))

;-------------
;HELPER FNS
;-------------
(defn convert-input
  "given a collection of two little endian two-byte numbers convert to a base 10 number"
  [input]
  (println "bout to be converted bb" input)
  (+ (first input) (* 256 (second input))))

(defn convert-output
  "given a base 10 number, convert it to a collection of two little endian two-byte numbers"
  [output]
  [(rem output 256) (quot output 256)])

(defn choose-input
  [input-idx]
  (println "which input am1?" input-idx @main-memory)
  (if (= input-idx input-1-start-index)
    (@main-memory :input-1)
    (@main-memory :input-2)))

(def choose-and-convert-input
  (comp convert-input choose-input))

;-------------
;MAIN
;-------------
; Clojure's data structures are immutable and it avoids state change when possible.
; I could've accomplished this in a more functional, Clojure-y way by passing the vars that need to be updated
; (main memory, register) as args to the loop and set them on each pass but i thought using [atoms](https://clojure.org/reference/atoms)
; would be clearer and in this case it's an appropriate application for them!
(defn virtual-machine
  [memory]
  (reset! main-memory (parse-memory memory))
  (let [instructions (get-main-memory :instructions)]
    (println "dem instructions" instructions)
    (loop []
      (let [current-instruction-idx (get-register program-counter-addr)]
        (println "CURRRENT INSTRUCTION" current-instruction-idx)
        (if (nil? current-instruction-idx)
          (get-main-memory :output)                         ; i know the instructions said not to return anything but i can't otherwise return the atom values
          (let [current-instruction (nth instructions current-instruction-idx)
                first-arg (nth instructions (inc current-instruction-idx) nil)
                second-arg (nth instructions (+ 2 current-instruction-idx) nil)]

            (case (instruction-set current-instruction)

              :load_word  (do (println "loading word" first-arg second-arg)
                              (load-word first-arg second-arg)
                              (set-register program-counter-addr (+ current-instruction-idx 3))
                              (recur))

              :store_word (do (println "storing word" first-arg second-arg)
                              (store-word first-arg second-arg)
                              (set-register program-counter-addr (+ current-instruction-idx 3))
                              (recur))

              :add        (do (println "adding" first-arg second-arg)
                              (add first-arg second-arg)
                              (set-register program-counter-addr (+ current-instruction-idx 3))
                              (recur))

              :sub        (do (println "subbing" first-arg second-arg)
                              (sub first-arg second-arg)
                              (set-register program-counter-addr (+ current-instruction-idx 3))
                              (recur))

              :halt       (do (println "halting")
                              (set-register program-counter-addr nil)
                              (recur)))))))))