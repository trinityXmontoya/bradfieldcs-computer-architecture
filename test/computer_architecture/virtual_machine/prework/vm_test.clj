(ns computer_architecture.virtual_machine.prework.vm_test
  (:require [clojure.test :refer :all]
            [computer_architecture.virtual_machine.prework.vm :refer :all]))

(deftest virtual-machine-test
  (are [output instructions] (= (do (virtual-machine instructions)
                                    (@main-memory :output))
                                (convert-output output))
                      5293 [0x01, 0x01, 0x14,               ; instruction 0 - set reg1 to input 1 (5281)
                            0x01, 0x02, 0x16,               ; instruction 3 - set reg2 to input 2 (12)
                            0x03, 0x01, 0x02,               ; instruction 6 - reg1 = reg1 + reg2
                            0x02, 0x01, 0x12,               ; instruction 9 - store val in reg1 (5293) at output
                            0xff,                           ; instruction 12 - halt
                            0x00, 0x00, 0x00, 0x00, 0x00,   ; unused instruction space
                            0x00, 0x00,                     ; output
                            0xa1, 0x14,                     ; input 1 (5281)
                            0x0c, 0x00]                     ; input 2 (12)


                      10562 [0x01, 0x01, 0x14,              ; instruction 0 - set reg1 to input 1 (5281)
                             0x01, 0x02, 0x14,              ; instruction 3 - set reg2 to input 1 (5281)
                             0x03, 0x01, 0x02,              ; instruction 6 - reg1 = reg1 + reg2
                             0x02, 0x01, 0x12,              ; instruction 9 - store val in reg1 (10562) at output
                             0xff,                          ; instruction 12 - halt
                             0x00, 0x00, 0x00, 0x00, 0x00,  ; unused instruction space
                             0x00, 0x00,                    ; output
                             0xa1, 0x14,                    ; input 1 (5281)
                             0x0c, 0x00]                    ; input 2 (12)

                      5281 [0x01, 0x01, 0x14,               ; instruction 0 - set reg1 to input 1 (5281)
                            0x17, 0x01, 0x01, 0xa,          ; instruction 3 - reg1 = reg1, so jump to instruction 10
                            0x01, 0x02, 0x16,               ; instruction 7 - set reg2 to input 2 (12)
                            0x02, 0x01, 0x12,               ; instruction 10 - store val in reg1 (5281) at output
                            0xff,                           ; instruction 13 - halt
                            0x00, 0x00, 0x00, 0x00          ; unused instruction space
                            0x00, 0x00,                     ; output
                            0xa1, 0x14,                     ; input 1 (5281)
                            0x0c, 0x00]                     ; input 2 (12)
                            ))

