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

# Byte ordering

1. If you saw port 8000 represented as 0x1f40, would you conclude that TCP uses big-endian or little-endian integers?

big-endian

to determine the hexadecimal representation of 8000 you would do
                           decimal -> hex
remainder of 8000 / 256 -> 64      -> 40
quotient  of 8000 / 256 -> 31      -> 1f

since this is 0x1f40 and not 0x401f, it means the most significant byte (1f) comes first and is therefore big-endian.

2. How would you represent port 3000?

0x0bb8
                           decimal  -> hex
remainder of 3000 / 256 -> 184      -> b8
quotient  of 3000 / 256 -> 11       -> 0b

# IEEE Floating Point

1. What are the largest and smallest values representable with 64-bit floats?

binary64 stores:
01 bits for the sign
11 bits for the exponent
53 bits for the coefficient

largest:   2^52
smallest: -2^53

# The Unicode Miracle

1. Is there any additional space cost to encoding a purely ASCII document as UTF-8?

Most space in ASCII is wasted because it starts at 65. And then lowercase starts 32 later rather than 26.

2. What are the pros and cons detriments of UTF-8 compared to another encoding for Unicode such as UTF-32?

The number after UTF-X indicates the max number of bits used to store characters (ex. UTF-8 -> 8 bits -> 1 byte, UTF-32 -> 32 bits -> 4 bytes).

UTF-32 doesn't require decoding and directly tells you how long string is.

UTF-8: Variable-width encoding, backwards compatible with ASCII. ASCII characters (U+0000 to U+007F) take 1 byte, code points U+0080 to U+07FF take 2 bytes, code points U+0800 to U+FFFF take 3 bytes, code points U+10000 to U+10FFFF take 4 bytes. Good for English text, not so good for Asian text.
UTF-16: Variable-width encoding. Code points U+0000 to U+FFFF take 2 bytes, code points U+10000 to U+10FFFF take 4 bytes. Bad for English text, good for Asian text.
UTF-32: Fixed-width encoding. All code points take four bytes. An enormous memory hog, but fast to operate on. Rarely used.

src: https://stackoverflow.com/questions/496321/utf-8-utf-16-and-utf-32