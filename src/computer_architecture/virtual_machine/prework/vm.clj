(ns computer_architecture.virtual_machine.prework.vm)

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
  (swap! registers assoc addr val))

(defn get-register
  [addr]
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
  (println "dat mem" memory)
  {:instructions (subvec memory instruction-start-index instruction-end-index)
   :output       (subvec memory output-start-index output-end-index)
   :input-1      (subvec memory input-1-start-index input-1-end-index)
   :input-2      (subvec memory input-2-start-index input-2-end-index)})

(defn load-word
  "Load value at given address into register"
  [reg input]
  (set-register reg input))

(defn store-word
  "Store the value in register at the given address"
  [reg addr]
  (get-register reg))

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
(defn third
  [coll]
  (nth coll 3))

(defn convert-input
  "given a collection of two little endian two-byte numbers we it to a base 10 number"
  [input]
  (+ (first input) (* 256 (second input))))

(defn choose-input
  [input-idx input-1 input-2]
  (if (= input-1-start-index input-idx)
    input-1
    input-2))

(def choose-and-convert-input
  (comp convert-input choose-input))

;-------------
;MAIN
;-------------
(defn virtual-machine
  [memory]
  (let [{:keys [instructions input-1 input-2]} (select-keys (parse-memory memory) [:instructions :input-1 :input-2])]
  (loop []
    (let [current-instruction-idx (get-register program-counter-addr)]

    (if (nil? current-instruction-idx)
      nil

      (let [ current-instruction (nth instructions current-instruction-idx)]
        (println "mah turn" current-instruction)

      (case (instruction-set current-instruction)

        :load_word  (do
                      (println "loading word")
                      (load-word (second instructions) (choose-and-convert-input (third instructions) input-1 input-2))
                      (set-register program-counter-addr (+ current-instruction-idx 3))
                      (recur))

        :store_word (do
                      (println "storing word")
                      (store-word (second instructions) (third instructions))
                      (set-register program-counter-addr (+ current-instruction-idx 3))
                      (recur))

        :add        (do
                      (println "adding")
                      (add (second instructions) (third instructions))
                      (set-register program-counter-addr (+ current-instruction-idx 3))
                      (recur))

        :sub        (do
                      (println "subbing")
                      (sub (second instructions) (third instructions))
                      (set-register program-counter-addr (+ current-instruction-idx 3))
                      (recur))

        ; clojure doesn't have a break statement. i could directly call java's System/exit
        ; but I think saying there are no more instructions is btr bc it's semantically correct
        :halt       (do
                      (println "halting")
                      (set-register program-counter-addr nil)
                      (recur)))))))))