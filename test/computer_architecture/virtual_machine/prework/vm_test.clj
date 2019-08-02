(ns computer_architecture.virtual_machine.prework.vm_test
  (:require [clojure.test :refer :all]
            [computer_architecture.virtual_machine.prework.vm :refer :all]))

(deftest virtual-machine-test
  (are [output instructions] (= (virtual-machine instructions) output)
                      5293 [
                         0x01, 0x01, 0x10,
                         0x01, 0x02, 0x12,
                         0x03, 0x01, 0x02,
                         0x02, 0x01, 0x0e,
                         0xff,
                         0x00,
                         0x00, 0x00,
                         0xa1, 0x14,
                         0x0c, 0x00
                         ]))

