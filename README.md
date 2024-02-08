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

*01010011 01100011 01100101 01101110  
01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101  
01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101  
01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000  
01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100  
01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100  
01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111  
00100000 01101000 01100101 01100001  
01110010 01100100 00101110*

At the end of this step we obtained a set of data with a length of 472 bits that is ready for the next step.

## Step 2: Append a single '1' to the end of the input data.

The SHA-256 hash function processes input data in 512-bit blocks, each divided into 16 words of 32 bits each. Therefore, with the binary form of the data for which we want to create a hash, we now need to process it into a format suitable for the algorithm.

We begin this process by appending a single '1' to the end of the source data:

01010011 01100011 01100101 01101110  
01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101  
01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101  
01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000  
01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100  
01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100  
01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111  
00100000 01101000 01100101 01100001  
01110010 01100100 00101110 *1*

## Step 3: Append data with padding consisting of multiple '0's. 

Next, we need to prepare our data so that its length is a multiple of 512 in bits. However, it's important to note that the last 64 bits are reserved for the total length of the source message, encoded as an 64-bit integer. So, after adding padding with zeros, our message should have a length x ≡ 448 mod 512. (where 448 comes from the length of the last 512-bit block minus 64 bits of source message length). 

In our case, we need to add 487 zeros:

01010011 01100011 01100101 01101110  
01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101  
01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101  
01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000  
01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100  
01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100  
01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111  
00100000 01101000 01100101 01100001  
01110010 01100100 00101110 1*0000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000*

## Step 4: Append the length of the original message to the end.

In this step, as mentioned earlier, we need to append the total length of the original message in bits as a 64-bit integer. Our original message had 472 bits, in binary this number is **111011000**.

Below is the fully prepared message:

01010011 01100011 01100101 01101110  
01100101 00100000 00110001 00100000  
01000001 00100000 01110100 01100101  
01101101 01110000 01100101 01110011  
01110100 01110101 01101111 01110101  
01110011 00100000 01101110 01101111  
01101001 01110011 01100101 00100000  
01101111 01100110 00100000 01110100  
01101000 01110101 01101110 01100100  
01100101 01110010 00100000 01100001  
01101110 01100100 00100000 01101100  
01101001 01100111 01101000 01110100  
01101110 01101001 01101110 01100111  
00100000 01101000 01100101 01100001  
01110010 01100100 00101110 10000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
00000000 00000000 00000000 00000000  
*00000000 00000000 00000000 00000000  
00000000 00000000 00000001 11011000*


<!-- Next steps to be added -->

<!--

## Step 5:
## Step 6:
## Step 7:
## Step 8:
## Step 9:
## Step 10:
## Step 11:
## Step 12: 

-->
