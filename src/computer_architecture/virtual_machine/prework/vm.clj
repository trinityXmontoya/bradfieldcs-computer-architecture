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
  (println "setting register" addr " to " val)
  (swap! registers assoc addr val))

(defn get-register
  [addr]
  (println "getting register" addr " current val - " (@registers addr))
  (@registers addr))

;-------------
;HELPER FNS
;-------------
(defn reset-registers!
  []
  (reset-vals! registers { program-counter-addr 0
                           register-1-addr nil
                           register-2-addr nil }))

(defn convert-input
  "given a collection of two little endian two-byte numbers convert to a base 10 number"
  [input]
  (+ (first input) (* 256 (second input))))

(defn convert-output
  "given a base 10 number, convert it to a collection of two little endian two-byte numbers"
  [output]
  [(rem output 256) (quot output 256)])

(declare input-1-start-index)
(defn choose-input
  [input-idx]
  (if (= input-idx input-1-start-index)
    (@main-memory :input-1)
    (@main-memory :input-2)))

(def choose-and-convert-input
  (comp convert-input choose-input))

;-------------
;INSTRUCTIONS
;-------------
(def instruction-set
  { 0x01 :load_word
    0x02 :store_word
    0x03 :add
    0x04 :sub
    0xff :halt
    0x17 :branch_if_equal})

(def instruction-len 18)
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

(defn load-word
  "Load value at given address into register"
  [args]
  (let [[reg addr] args
        input (choose-and-convert-input addr)]
    (set-register reg input)))

(defn store-word
  "Store the value in register at the given address"
  [args]
  (let [[reg addr] args
        output (convert-output (get-register reg))]
    (set-main-memory-output output)))

(defn add
  "Set reg1 = reg1 + reg2"
  [args]
  (let [[reg1 reg2] args]
    (set-register reg1 (+ (get-register reg1) (get-register reg2)))))

(defn sub
  "Set reg1 = reg1 - reg2"
  [args]
  (let [[reg1 reg2] args]
    (set-register reg1 (- (get-register reg1) (get-register reg2)))))

(defn halt
  "sets the program counter to nil"
  []
  (set-register program-counter-addr nil))

(defn branch-if-equal
  "changes the program counter to the given address if the register values are equal"
  [args]
  (let [[reg1 reg2 addr] args]
    (when (= reg1 reg2)
      (set-register program-counter-addr addr))))

;-------------
;MAIN
;-------------
(defn execute-and-increment
  "sets the program counter to the next instruction, then performs the specified fn with the given arguments"
  [fn current-instruction-idx amt-to-increment & args]
  (set-register program-counter-addr (+ current-instruction-idx amt-to-increment))
  (fn args))

; I could've accomplished this in a more functional, Clojure-y way by passing the vars that need to be updated
; (main memory, register) as args to the loop and set them on each pass but i thought using [atoms](https://clojure.org/reference/atoms)
; would be clearer and in this case it's an appropriate application for them!
(defn virtual-machine
  [memory]
  ; SETUP
  (reset-registers!)
  (reset! main-memory (parse-memory memory))
  ; RUN
  (while (not (nil? (get-register program-counter-addr)))
      (let [instructions (get-main-memory :instructions)
            current-instruction-idx (get-register program-counter-addr)]
          (let [current-instruction (nth instructions current-instruction-idx)
                first-arg           (nth instructions (inc current-instruction-idx) nil)
                second-arg          (nth instructions (+ 2 current-instruction-idx) nil)
                third-arg           (nth instructions (+ 3 current-instruction-idx) nil)]
            (case (instruction-set current-instruction)
              :load_word       (execute-and-increment load-word       current-instruction-idx 3 first-arg second-arg)
              :store_word      (execute-and-increment store-word      current-instruction-idx 3 first-arg second-arg)
              :add             (execute-and-increment add             current-instruction-idx 3 first-arg second-arg)
              :sub             (execute-and-increment sub             current-instruction-idx 3 first-arg second-arg)
              :branch_if_equal (execute-and-increment branch-if-equal current-instruction-idx 4 first-arg second-arg third-arg)
              :halt            (halt))))))