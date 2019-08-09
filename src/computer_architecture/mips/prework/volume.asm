# please write a program which can calculate the volume of a cylinder given its height and radius.
      .data
pi:   .word 3
h:    .word 4
r:    .word 2
      .text
      la  $t0, pi
      la  $t1, h
      la  $t2, r
      mul $t3, $t0, $t1
      mul $t4, $t2, $t2
      mul $s0, $t3, $t4
      jal  print            # call print routine.
      li   $v0, 10          # system call for exit
      syscall               # we are out of here.

# print
      .data
head: .asciiz  "The volume of this cylinder is:\n"
      .text
print:la   $a0, head        # load address of print heading
      li   $v0, 4           # specify Print String serv
      syscall               # print heading
      lw   $a0, ($s0)       # load volume for syscall
      li   $v0, 1           # specify Print Integer service
      syscall               # print volume number