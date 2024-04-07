# Java-Sha256
This repository contains a Java implementation of the SHA-256 algorithm. The priority of writing this implementation was clarity and readability to easily understand the algorithm.

### If you're looking for how to implement the SHA 256 algorithm in your Java project, just use:

```java
  MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
  byte[] hash = messageDigest.digest(source);
```

Code in this project is divided into two files **Main.java** with main function and two uses of the implemented algorithm for generating a hash from string input or from a file. 
Second file **Sha256MessageDigest.java** contains the whole implementation of the SHA-256 algorithm with marked each step that is crucial for calculating the hash value. 

# SHA-256 Algorithm

Below, you'll find an explanation of the SHA-256 algorithm, breaking down each step needed to implement it. Every step described below is marked in **Sha256MessageDigest** class for easier tracking and understanding. The algorithm was described by hashing the beginning of the book 'The Tempest', because why not. The text: "Scene 1 A tempestuous noise of thunder and lightning heard."

## Step 1: Convert the input data into its binary representation. 

The SHA256 algorithm operates on the binary form of input data, so first we need to prepare the data. Files are already stored as bytes, but for text data, we need to perform a conversion. By default, UTF-8 encoding is commonly used for this purpose, but there's nothing preventing us from using other character encoding systems such as UTF-16LE, UTF-16BE, or any other encoding system. We will use UTF-8.

Character | Binary Representation
--------- | ---------------------
S         | 01010011  
c         | 01100011
e         | 01100101
n         | 01101110
e         | 01100101
(space)   | 00100000
1         | 00110001

and so on...

Next lets concatenate the binary representation of each letter into single block of data:

*01010011 01100011 01100101 01101110 01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101 01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101 01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000 01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100 01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100 01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111 00100000 01101000 01100101 01100001  
01110010 01100100 00101110*

At the end of this step we obtained a set of data with a length of 472 bits that is ready for the next step.

## Step 2: Append a single '1' to the end of the input data.

The SHA-256 hash function processes input data in 512-bit blocks, each divided into 16 words of 32 bits each. Therefore, with the binary form of the data for which we want to create a hash, we now need to process it into a format suitable for the algorithm.

We begin this process by appending a single '1' to the end of the source data:

01010011 01100011 01100101 01101110 01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101 01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101 01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000 01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100 01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100 01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111 00100000 01101000 01100101 01100001  
01110010 01100100 00101110 *1*

## Step 3: Append data with padding consisting of multiple '0's.

Next, we need to prepare our data so that its length is a multiple of 512 in bits. However, it's important to note that the last 64 bits are reserved for the total length of the source message, encoded as an 64-bit integer. So, after adding padding with zeros, our message should have a length x ≡ 448 mod 512. (where 448 comes from the length of the last 512-bit block minus 64 bits of source message length). 

In our case, we need to add 487 zeros:

01010011 01100011 01100101 01101110 01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101 01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101 01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000 01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100 01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100 01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111 00100000 01101000 01100101 01100001  
01110010 01100100 00101110 1*0000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000*

## Step 4: Append the length of the original message to the end.

In this step, as mentioned earlier, we need to append the total length of the original message in bits as a 64-bit integer. Our original message had 472 bits, in binary this number is **111011000**.

Below is the fully prepared message:

01010011 01100011 01100101 01101110 01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101 01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101 01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000 01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100 01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100 01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111 00100000 01101000 01100101 01100001  
01110010 01100100 00101110 10000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
*00000000 00000000 00000000 00000000 00000000 00000000 00000001 11011000*

## Step 5: Break the prepared message into 512 bit blocks.

Now, at this step, we need to divide our message into 512-bit blocks, each of which will be then processed individually using a set of bitwise operations, to finally calculate a 256-bit hash using the obtained values.

At the end of the previous steps, we should get a message that's length is a multiple of 512 bits. If it's not, some error must have been made, most likely while adding padding to the original message on **Step 3**.

Our example prepared message had a length of 1024 bits, so after dividing it, we received two 512 bit blocks shown below:

Block 1:

01010011 01100011 01100101 01101110 01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101 01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101 01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000 01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100 01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100 01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111 00100000 01101000 01100101 01100001  
01110010 01100100 00101110 10000000 00000000 00000000 00000000 00000000

Block 2:

00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000001 11011000

## Step 6: Prepare message schedule.

Now we move on to the main part of the algorithm. In the first part, for each 512-bit block received in the previous steps, we need to prepare a message schedule consisting of 64 words, each 32 bits long.

These message schedules will be used for decomposing each of the blocks. This step is done to maximize the spreading out of input changes throughout the output and to make the relationship between input and output complex and difficult to predict. 

In the subsequent steps, I will provide examples for both blocks. Especially in the case of the second block, which consists mostly of zeros with single ones at the end, the resulting message schedule after computation will be completely unpredictable.

Now, the empty message schedule for Block 1 looks as follows:

W0: 00000000 00000000 00000000 00000000  
W1: 00000000 00000000 00000000 00000000  
W2: 00000000 00000000 00000000 00000000  
W3: 00000000 00000000 00000000 00000000  
...  
W63: 00000000 00000000 00000000 00000000

## Step 7: Divide the block and assign it into the first 16 words of the message schedule.

In this step, we need to divide our 512-bit block into 16 32-bit sub-blocks and assign them sequentially to the first 16 words of the message scheduler.

For the Block 1:

W0: 01010011 01100011 01100101 01101110  
W1: 01100101 00100000 00110001 00100000  
W2: 01000001 00100000 01110100 01100101  
W3: 01101101 01110000 01100101 01110011  
W4: 01110100 01110101 01101111 01110101  
W5: 01110011 00100000 01101110 01101111  
W6: 01101001 01110011 01100101 00100000  
W7: 01101111 01100110 00100000 01110100  
W8: 01101000 01110101 01101110 01100100  
W9: 01100101 01110010 00100000 01100001  
W10: 01101110 01100100 00100000 01101100  
W11: 01101001 01100111 01101000 01110100  
W12: 01101110 01101001 01101110 01100111  
W13: 00100000 01101000 01100101 01100001  
W14: 01110010 01100100 00101110 10000000  
W15: 00000000 00000000 00000000 00000000  
W16: 00000000 00000000 00000000 00000000  
W17: 00000000 00000000 00000000 00000000  
W18: 00000000 00000000 00000000 00000000  
...  
W63: 00000000 00000000 00000000 00000000

Words 0 through 15 have been assigned as initial values from the 512-bit block. Subsequent words from 16 to 63 will be calculated using simple bitwise operations on the previous words, such as rotation, shifting, or XOR operations.

## Step 8: Calculate the remaining 16 to 63 words of the message scheduler.

Starting from word 16, each subsequent word must be computed using the previous ones and the following formula:

```
Wj = Wj-16 + σ0(Wj-15) + Wj-7 + σ1(Wj-2)
```

Where, from the beginning, Wj represents the word being currently calculated, 'j' is its index position in the message scheduler, Wj-n represents the word at index j-n, and σ0 and σ1 are functions operating on a single word.

**A crucial point to mention is that the resulting word Wj must be of size 32 bits to be assigned back to the message scheduler. So, the operation '+' signifies mod 2<sup>32</sup> addition.**

Below is the description of functions σ0 and σ1. Both functions utilize two bitwise rotation operations, bitwise shifting, and XOR-ing the results of these operations, but each with a slightly different number of bits.

function **σ0**:

```
σ0(X) = RotR(X, 7) ⊕ RotR(X, 18) ⊕ (X >> 3)
```

- **RotR(X, 7)** Right rotation (circular shift) of the input word by 7 bits.
- **RotR(X, 18)** Another right rotation of the input word by 18 bits.
- **(X >> 3)** Right shift of the input word by 3 bits
- **⊕** Finally, it performs a bitwise XOR operation between the results of the previous operations.

function **σ1**: (It is very similar, differing only in the number of bits in the operations.)

```
σ1(X) = RotR(X, 17) ⊕ RotR(X, 19) ⊕ (X >> 10)
```
---------------------
### Knowing the formulas, let's calculate the first, i.e., the 16th word in the message schedule. 

After replacing the index 'j' with the number 16 in the formula, we get:

```
W16 = W0 + σ0(W1) + W9 + σ1(W14)
```

We will need the following words for calculations:

W0: 01010011 01100011 01100101 01101110  
W1: 01100101 00100000 00110001 00100000  
W9: 01100101 01110010 00100000 01100001  
W14: 01110010 01100100 00101110 10000000   

Let's calculate the σ0 and σ1:

computations for σ0(W1):
```
01100101 00100000 00110001 00100000 - W1  

01000000 11001010 01000000 01100010 - right rotate 7  
00001100 01001000 00011001 01001000 - right rotate 18  
00001100 10100100 00000110 00100100 - right shift 3  

01000000 00100110 01011111 00001110 - σ0(W1) // after XOR-ing above results
```
computations for σ1(W14):
```
01110010 01100100 00101110 10000000 - W14  

00010111 01000000 00111001 00110010 - right rotate 17  
10000101 11010000 00001110 01001100 - right rotate 19  
00000000 00011100 10011001 00001011 - right shift 10  

10010010 10001100 10101110 01110101 - σ1(W14) // after XOR-ing above results
```
After calculating σ0 and σ1, we can compute the word W16:
```
01010011 01100011 01100101 01101110 - W0  
01000000 00100110 01011111 00001110 - σ0(W1)  
01100101 01110010 00100000 01100001 - W9  
10010010 10001100 10101110 01110101 - σ1(W14)

10001011 10001000 10010011 01010010 - W16 // after mod 2^32 addition of above results
```

At the end, we obtained the 16th word, and we can now put it into the message schedule. Now we need to repeat this step for the subsequent words up to 63rd until we have the entire message schedule prepared.

The subsequent words, as shown earlier, are calculated using previously calculated words. Therefore, there isn't much opportunity for parallelization, and the entire message schedule must be computed sequentially. But, while the computation of the message schedule for a single block may be sequential, multiple blocks can be processed in parallel by different threads or processes. This can significantly speed up the hashing process, especially for large messages where multiple blocks are involved.

Since I think it doesn't make sense to list 2048 ones and zeros here for the first block and another 2048 for the second, it's worth showing how the subsequent words in the message schedule look for a block that was filled with mostly zeros with a few ones at the end.

Below are the last 10 words for the block 2.

W54: 10001100 11111010 10000001 01101100  
W55: 11111111 10101101 11000111 10100010  
W56: 01011110 10000100 00000111 01011000  
W57: 01111010 10100000 11100100 11010011  
W58: 11111001 11111001 01110101 10000001  
W59: 11000111 00000000 01111010 00001000  
W60: 10000110 11011111 10101001 00110000  
W61: 10001100 10111000 00000001 01000100  
W62: 10010100 10101000 11011101 00101011  
W63: 10111001 00111001 01100101 00001110  

As you can see, they are filled with values that are impossible to predict, and changing even one more zero to a one in that block would completely alter above values.

## Step 9: Prepare the initial hash value for the new set of words.

In this step, we begin the actual computation of the hash value. 

At the beginning of computing the hash value for a new set of words, we assign the hash values to 8 32-bit registers a, b, c, d, e, f, g, h, calculated using the previous set or initial values if it's the first set being computed.

### Calculating the initial values for the first block.

The initial values are obtained by taking the first 32 bits of the fractional part of the square roots of the first 8 prime numbers 2, 3, 5, 7, 11, 13, 17 and 19.

H<sub>0</sub><sup>(1)</sup>: √2 = 1.414213562373095... = 1.01101010000010011110011001100111... = 01101010 00001001 11100110 01100111  

First, we take the nth prime number: **2**  
We take the square root of that number: **√2**  
We calculate the decimal representation of that square root: **1.414213562373095...**  
We convert the decimal representation to binary form: **1.01101010000010011110011001100111...**  
We take the first 32 bits of the fractional part of the obtained square root in binary form: **01101010 00001001 11100110 01100111**

The obtained number is our initial value for the first register. Remaining values are calculated below.

H<sub>0</sub><sup>(2)</sup>: √3 = 1.732050807568877... = 1.10111011011001111010111010000101... => 10111011 01100111 10101110 10000101  
H<sub>0</sub><sup>(3)</sup>: √5 = 2.23606797749979... = 10.00111100011011101111001101110010... => 00111100 01101110 11110011 01110010  
H<sub>0</sub><sup>(4)</sup>: √7 = 2.645751311064591... = 10.10100101010011111111010100111010... => 10100101 01001111 11110101 00111010  
H<sub>0</sub><sup>(5)</sup>: √11 = 3.3166247903554... = 11.01010001000011100101001001111111... => 01010001 00001110 01010010 01111111  
H<sub>0</sub><sup>(6)</sup>: √13 = 3.605551275463989... = 11.10011011000001010110100010001100... => 10011011 00000101 01101000 10001100  
H<sub>0</sub><sup>(7)</sup>: √17 = 4.123105625617661... = 100.00011111100000111101100110101011... => 00011111 10000011 11011001 10101011  
H<sub>0</sub><sup>(8)</sup>: √19 = 4.358898943540674... = 100.01011011111000001100110100011001... => 01011011 11100000 11001101 00011001  

In implementations of the algorithm, it doesn't make sense to calculate these values every time a new hash value is computed. They are usually hard-coded into the program's code, often in hexadecimal form:

H<sub>0</sub><sup>(1)</sup>: 6A09 E667  
H<sub>0</sub><sup>(2)</sup>: BB67 AE85  
H<sub>0</sub><sup>(3)</sup>: 3C6E F372  
H<sub>0</sub><sup>(4)</sup>: A54F F53A  
H<sub>0</sub><sup>(5)</sup>: 510E 527F  
H<sub>0</sub><sup>(6)</sup>: 9B05 688C  
H<sub>0</sub><sup>(7)</sup>: 1F83 D9AB  
H<sub>0</sub><sup>(8)</sup>: 5BE0 CD19  

These values are only being used during the calculation of the hash value for the first block as initial values. For the next block, the starting values will be the values calculated at the end of the previous block, as will be shown in the following steps.

At the end of this step, we assign values to the registers calculated in the previous step 'i - 1', or for the first step initial values calculated earlier.

a = H<sub>i-1</sub><sup>(1)</sup>  
b = H<sub>i-1</sub><sup>(2)</sup>  
c = H<sub>i-1</sub><sup>(3)</sup>  
d = H<sub>i-1</sub><sup>(4)</sup>  
e = H<sub>i-1</sub><sup>(5)</sup>  
f = H<sub>i-1</sub><sup>(6)</sup>  
g = H<sub>i-1</sub><sup>(7)</sup>  
h = H<sub>i-1</sub><sup>(8)</sup>  

## Step 10: Calculate the intermediate hash value for each block.

With registers from a to h prepared, we can proceed to the main computation of the hash value in the SHA-256 algorithm.

In this step, for each word 'Wj' in the message scheduler a total of 64 times, we perform the following operations.

h = g  
g = f  
f = e  
e = d + Temp1  
d = c  
c = b  
b = a  
a = Temp1 + Temp2  

where:

Temp1 = h + Σ1(e) + Choice(e, f, g) + Kj + Wj  
Temp2 = Σ0(a) + Majority(a, b, c)  

Now we need to explain what Σ0 and Σ1 represent, as well as K and the Choice and Majority functions.

----------------------------
Let's start with K. These are 64 32-bit words obtained through a very similar process to the initial values H<sub>0</sub> from the previous step. But this time, we use cube roots instead of square roots.

This time the constant K is calculated as the first 32 bits of the fractional part of the **cube roots** of the first 64 prime numbers. 

K<sup>(1)</sup>: <sup>3</sup>√2 = 1.25992104989... = 1.01000010100010100010111110011000... => 01000010 10001010 00101111 10011000 = **428A 2F98**  

-----------------------------------------------
Now, functions Σ0 and Σ1. They are quite similar to σ0 and σ1, but they do not involve right shifts, only right rotations and XOR operations on the results of these rotations.

computations for Σ0(a):
```
01101010 00001001 11100110 01100111 - a

11011010 10000010 01111001 10011001 - right rotate 2  
00110011 00111011 01010000 01001111 - right rotate 13  
00100111 10011001 10011101 10101000 - right rotate 22

11001110 00100000 10110100 01111110 - Σ0(a) // after XOR-ing above results
```

computations for Σ1(e):
```
01010001 00001110 01010010 01111111 - e

11111101 01000100 00111001 01001001 - right rotate 6
01001111 11101010 00100001 11001010 - right rotate 11
10000111 00101001 00111111 10101000 - right rotate 25

00110101 10000111 00100111 00101011 - Σ1(e) // after XOR-ing above results
```

-----------------------------------
Now let's talk about the function Choice.

It operates on three 32-bit words, always registers e, f and g. The choice function selects bits from f or g based on the value of the corresponding bit in e.

In other words: For each bit position, it chooses the bit from register e. If the bit is 1, It sets the result bit to corresponding bit from register f. If the bit from e was 0 it takes the bit from register g.

Mathematically, it can be expressed as:
```
Choice(e, f, g) = (e ∧ f) ⊕ ((¬e) ∧ g)
```
where:

- ∧ denotes the bitwise AND operation.
- ¬ denotes the bitwise NOT operation.
- ⊕ denotes the bitwise XOR operation.

Let's calculate the value of this function for our example.
```
01010001 00001110 01010010 01111111 - e
10011011 00000101 01101000 10001100 - f
00011111 10000011 11011001 10101011 - g

00011111 10000101 11001001 10001100 - Choice(e, f, g)
```

-------------------------------------
Now the last function to explain, Majority.

It also operates on three 32-bit words, this time always registers a, b and c.

For each bit position, it computes the majority of the bits at that position among the three input words. If there are more '0' than '1' bits, the result bit is set to '0' otherwise, it is set to '1'.

Mathematically, the majority function can be expressed as:
```
Majority(a, b, c) = (a ∧ b) ⊕ (a ∧ c) ⊕ (b ∧ c)
```

computations for Majority(a, b, c)
```
01101010 00001001 11100110 01100111 - a
10111011 01100111 10101110 10000101 - b
00111100 01101110 11110011 01110010 - c

00111010 01101111 11100110 01100111 - Majority(a, b, c)
```

Now that we have explained all the functions, let's calculate updated register values for the first word Wj in the message scheduler for the first block.

Just to remind you, the register values are: (these are the values calculated in step 9)

a: 01101010 00001001 11100110 01100111  
b: 10111011 01100111 10101110 10000101  
c: 00111100 01101110 11110011 01110010  
d: 10100101 01001111 11110101 00111010  
e: 01010001 00001110 01010010 01111111  
f: 10011011 00000101 01101000 10001100  
g: 00011111 10000011 11011001 10101011  
h: 01011011 11100000 11001101 00011001  

First, we need to calculate the values of Temp1 and Temp2.

computations for Temp1
```
01011011 11100000 11001101 00011001 - h
00110101 10000111 00100111 00101011 - Σ1(e)
00011111 10000101 11001001 10001100 - Choice(e, f, g)
01000010 10001010 00101111 10011000 - Kj
01010011 01100011 01100101 01101110 - Wj

01000110 11011011 01010010 11010110 - Temp1 // after mod 2^32 addition of above values
```

computations for Temp2
```
11001110 00100000 10110100 01111110 - Σ0(a)
00111010 01101111 11100110 01100111 - Majority(a, b, c)

00001000 10010000 10011010 11100101 - Temp2 // after mod 2^32 addition of above values
```

Now we can assign new values to the registers.

a = 01001111 01101011 11101101 10111011 = Temp1 + Temp2  
b = 01101010 00001001 11100110 01100111 = a<sup> j-1</sup>  
c = 10111011 01100111 10101110 10000101 = b<sup> j-1</sup>  
d = 00111100 01101110 11110011 01110010 = c<sup> j-1</sup>  
e = 11101100 00101011 01001000 00010000 = d<sup> j-1</sup> + Temp1  
f = 01010001 00001110 01010010 01111111 = e<sup> j-1</sup>  
g = 10011011 00000101 01101000 10001100 = f<sup> j-1</sup>  
h = 00011111 10000011 11011001 10101011 = g<sup> j-1</sup>  

Now that we have updated register values for the first word, we need to repeat the above steps for the remaining words from 2 to 64.

## Step 11: Calculate the new hash values for the computed block.

After completing the calculations from the previous step for each word up to 64th, we can proceed to calculate the intermediate hash value, which will serve as the initial value for the next block, or if it was the last block, as the final result of the algorithm.

To do this, simply add the register values from a to h to the initial values with which we started the calculations for this block.

H<sub>i</sub><sup>(1)</sup> = H<sub>i-1</sub><sup>(1)</sup> + a  
H<sub>i</sub><sup>(2)</sup> = H<sub>i-1</sub><sup>(2)</sup> + b  
H<sub>i</sub><sup>(3)</sup> = H<sub>i-1</sub><sup>(3)</sup> + c  
H<sub>i</sub><sup>(4)</sup> = H<sub>i-1</sub><sup>(4)</sup> + d  
H<sub>i</sub><sup>(5)</sup> = H<sub>i-1</sub><sup>(5)</sup> + e  
H<sub>i</sub><sup>(6)</sup> = H<sub>i-1</sub><sup>(6)</sup> + f  
H<sub>i</sub><sup>(7)</sup> = H<sub>i-1</sub><sup>(7)</sup> + g  
H<sub>i</sub><sup>(8)</sup> = H<sub>i-1</sub><sup>(8)</sup> + h  

Below are the calculated values for the first block 'i = 1', and these values will be used as the initial values for calculating next intermediate hash value from the next block.

H<sub>1</sub><sup>(1)</sup> = 01101010 00001001 11100110 01100111 + 10110010 00111000 00000000 01101001 = 00011100 01000001 11100110 11010000  
H<sub>1</sub><sup>(2)</sup> = 10111011 01100111 10101110 10000101 + 11011101 00010001 11100100 01101001 = 10011000 01111001 10010010 11101110  
H<sub>1</sub><sup>(3)</sup> = 00111100 01101110 11110011 01110010 + 00110100 01101010 11011011 10100111 = 01110000 11011001 11001111 00011001  
H<sub>1</sub><sup>(4)</sup> = 10100101 01001111 11110101 00111010 + 01100100 01000110 11010111 11110000 = 00001001 10010110 11001101 00101010  
H<sub>1</sub><sup>(5)</sup> = 01010001 00001110 01010010 01111111 + 01110111 00101010 10100000 01110100 = 11001000 00111000 11110010 11110011  
H<sub>1</sub><sup>(6)</sup> = 10011011 00000101 01101000 10001100 + 01010010 01111101 10000110 11010011 = 11101101 10000010 11101111 01011111  
H<sub>1</sub><sup>(7)</sup> = 00011111 10000011 11011001 10101011 + 01011101 00011010 10101100 11011110 = 01111100 10011110 10000110 10001001  
H<sub>1</sub><sup>(8)</sup> = 01011011 11100000 11001101 00011001 + 01101000 10101000 00110101 10111111 = 11000100 10001001 00000010 11011000  

## Step 12: Calculate the final hash value.

If all sets have been calculated for all blocks, the final hash value is the concatenation of eight 32-bit blocks from the H<sub>n</sub> array (n - number of blocks).

The values calculated at the end of the second (last) block:

H<sub>n</sub><sup>(1)</sup> = 00010010 10110111 11110110 00100011 = 12B7 F623  
H<sub>n</sub><sup>(2)</sup> = 10111011 11011011 10101100 01001000 = BBDB AC48  
H<sub>n</sub><sup>(3)</sup> = 11000000 11101101 11111111 11010101 = C0ED FFD5  
H<sub>n</sub><sup>(4)</sup> = 00011000 10101110 01000011 00101000 = 18AE 4328  
H<sub>n</sub><sup>(5)</sup> = 00111110 11010111 11010001 10100110 = 3ED7 D1A6  
H<sub>n</sub><sup>(6)</sup> = 11100001 11011111 00101000 10000101 = E1DF 2885  
H<sub>n</sub><sup>(7)</sup> = 01111000 00011001 01001101 11001100 = 7819 4DCC  
H<sub>n</sub><sup>(8)</sup> = 01111000 00000101 01111000 10001110 = 7805 788E  

Now all we need to do is concatenate these values into a single sequence.

SHA-256 hash: H<sub>n</sub><sup>(1)</sup> || H<sub>n</sub><sup>(2)</sup> || H<sub>n</sub><sup>(3)</sup> || H<sub>n</sub><sup>(4)</sup> || H<sub>n</sub><sup>(5)</sup> || H<sub>n</sub><sup>(6)</sup> || H<sub>n</sub><sup>(7)</sup> || H<sub>n</sub><sup>(8)</sup>

SHA-256 hash: 12B7 F623 BBDB AC48 C0ED FFD5 18AE 4328 3ED7 D1A6 E1DF 2885 7819 4DCC 7805 788E

# Special Thanks

The materials thanks to which I learned how the algorithm works:  
https://helix.stormhub.org/papers/SHA-256.pdf  
https://eips.ethereum.org/assets/eip-2680/sha256-384-512.pdf  

The algorithm demonstration I used to compute individual values in the above README.md:  
https://sha256algorithm.com/
