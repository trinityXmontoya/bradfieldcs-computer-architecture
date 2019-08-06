# x15 and Hexadecimal numbers

1. test yourself by converting the numbers 9, 136 and 247 to hexadecimal.
9   -> 9
136 -> 88
247 -> f7
 
2. In CSS, how many colors can be represented in the hexadecimal form?

6 characters available x 4 bits that can be represented for each character
= 24 bits
= 2^24
= 16,777,216

(or using Oz's powers of 2 magic:
2^24
= 2^4 * 2^10 * 2^10
= 16 * 1000 * 1000
= ~16 mil
)

3. How about in the RGB form?

Same as CSS! It's just a different representation using 0 - 255 for each of the colors (R,G,B)

4. If you were given a hex code, could you guess the color?

Perhaps. Off the top of my head I know
#000000 is black
#ffffff is white
#ff0000 is red
#00ff00 is green
#0000ff is blue
so the first two characters are red, the second two are green, and the third are blue. 

For example
#8FC808
C8 (green) > 8F (red) > 08 (blue)
but I don't know what color green + red produce (the answer is yellow!). I am only ok at [this game](http://yizzle.com/whatthehex)

# Binary Addition & Overflow

1. Test yourself by converting the numbers 12 and 9 to binary, adding the binary values together, and converting the result back to decimal to verify your calculation. Do these by hand.

  0000 1001 -> 9
+ 0000 1100 -> 12
= 0001 0101 -> 21

2. What would the result be if it were constrained to 4 bits?

you're trying to represent in 4 bits what requires 5 bits so you would have an overflow. It would incorrectly be stored as 0101 (or 5) bc that's all the digits it can fit.

3. How many numbers can be represented in total with 4, 8, 16 or 32 bits respectively?

4  -> 2^4  -> 16
8  -> 2^8  -> 256
16 -> 2^16 -> 65,536
32 -> 2^32 -> 4,294,967,296

# Why We Use Two’s Complement

1. test yourself by converting the numbers 12 and -9 to binary using the two’s complement representation, adding the binary values together, and converting the result back to decimal. 

   01100 -> +12
+  10111 -> -09
=  00011 -> +03

2. Similarly compute -3 - 4 ("negative three, minus four"). 
   1101 -> -03
-  0100 -> +04
=  1001 -> -07

3. What are the largest and smallest numbers representable in 32 bit 2’s complement?
32 bits -> 2^32 #s,

largest:  2,147,483,647  ((2^32 / 2) - 1)
smallest: -2,147,483,648 (2^32 / 2)