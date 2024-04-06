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

## Step 5: Break the prepared message into 512 bit chunks.

Now, at this step, we need to divide our message into 512-bit chunks, each of which will be then processed individually using a set of bitwise operations, to finally calculate a 256-bit hash using the obtained values.

At the end of the previous steps, we should get a message that's length is a multiple of 512 bits. If it's not, some error must have been made, most likely while adding padding to the original message on **Step 3**.

Our example prepared message had a length of 1024 bits, so after dividing it, we received two 512 bit chunks shown below:

Chunk 1:

01010011 01100011 01100101 01101110 01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101 01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101 01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000 01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100 01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100 01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111 00100000 01101000 01100101 01100001  
01110010 01100100 00101110 10000000 00000000 00000000 00000000 00000000

Chunk 2:

00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000 00000000 00000000 00000001 11011000

## Step 6: Prepare message schedule

Now we move on to the main part of the algorithm. In the first part, for each 512-bit block received in the previous steps, we need to prepare a message schedule consisting of 64 words, each 32 bits long.

These message schedules will be used for decomposing each of the blocks. This step is done to maximize the spreading out of input changes throughout the output and to make the relationship between input and output complex and difficult to predict. 

In the subsequent steps, I will provide examples for both blocks. Especially in the case of the second block, which consists mostly of zeros with single ones at the end, the resulting message schedule after computation will be completely unpredictable.

Now, the empty message schedule for Chunk 1 looks as follows:

0: 00000000 00000000 00000000 00000000  
1: 00000000 00000000 00000000 00000000  
2: 00000000 00000000 00000000 00000000  
3: 00000000 00000000 00000000 00000000  
...  
63: 00000000 00000000 00000000 00000000

## Step 7: Divide the block and assign it into the first 16 words of the message schedule

In this step, we need to divide our 512-bit block into 16 32-bit sub-blocks and assign them sequentially to the first 16 words of the message scheduler.

For the Chunk 1:

0: 01010011 01100011 01100101 01101110  
1: 01100101 00100000 00110001 00100000  
2: 01000001 00100000 01110100 01100101  
3: 01101101 01110000 01100101 01110011  
4: 01110100 01110101 01101111 01110101  
5: 01110011 00100000 01101110 01101111  
6: 01101001 01110011 01100101 00100000  
7: 01101111 01100110 00100000 01110100  
8: 01101000 01110101 01101110 01100100  
9: 01100101 01110010 00100000 01100001  
10: 01101110 01100100 00100000 01101100  
11: 01101001 01100111 01101000 01110100  
12: 01101110 01101001 01101110 01100111  
13: 00100000 01101000 01100101 01100001  
14: 01110010 01100100 00101110 10000000  
15: 00000000 00000000 00000000 00000000  
16: 00000000 00000000 00000000 00000000  
17: 00000000 00000000 00000000 00000000  
18: 00000000 00000000 00000000 00000000  
...  
63: 00000000 00000000 00000000 00000000

Words 0 through 15 have been assigned as initial values from the 512-bit block. Subsequent words from 16 to 63 will be calculated using simple bitwise operations on the previous words, such as rotation, shifting, or XOR operations.

## Step 8: Calculating the remaining 16 to 63 words of the message scheduler

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

<!-- Next steps to be added -->

<!--

## Step 9:
## Step 10:
## Step 11:
## Step 12: 

-->
