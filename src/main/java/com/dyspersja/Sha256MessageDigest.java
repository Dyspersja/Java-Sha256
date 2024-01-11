package com.dyspersja;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Sha256MessageDigest {

    // The K constants
    // 64 32-bit values obtained by taking the first 32 bits of the
    // fractional parts of the cube roots of the first 64 primes
    private final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    // Initial Hash values
    // 8 32-bit values obtained by taking the first 32 bits of the
    // factorial parts of the square roots of the first 8 prime numbers
    private final int[] H0 = {
            0x6a09e667,
            0xbb67ae85,
            0x3c6ef372,
            0xa54ff53a,
            0x510e527f,
            0x9b05688c,
            0x1f83d9ab,
            0x5be0cd19
    };

    public byte[] digest(String input) {
        // Step 1: Encode the input to binary using UTF-8
        byte[] sourceBytes = input.getBytes(StandardCharsets.UTF_8);

        return generate(sourceBytes);
    }

    public byte[] digest(byte[] sourceBytes) {
        return generate(sourceBytes);
    }

    private byte[] generate(byte[] sourceBytes) {
        // Prepare the message, make its length a multiple of 512
        byte[] messageBlock = createMessageBlock(sourceBytes);
        // Step 5: Break prepared message into 512-bit chunks
        byte[][] chunks = breakIntoChunks(messageBlock);
        // Create message schedules with data from chunks
        int[][] messageSchedules = createMessageSchedules(chunks);
        // Compute words from 16th to 64th in each message schedule
        int[][] expandedMessageSchedules = computeMessageSchedules(messageSchedules);
        // Calculate the final 256-bit hash value
        return calculateHash(expandedMessageSchedules);
    }

    private byte[] createMessageBlock(byte[] sourceBytes) {
        int paddingLength = calculatePaddingLength(sourceBytes.length);
        int paddedLength = sourceBytes.length + paddingLength + (64 / 8);

        ByteBuffer buffer = ByteBuffer.allocate(paddedLength);
        // Initial raw bytes of source data
        buffer.put(sourceBytes);
        // Step 2: Append the data with a single '1'
        buffer.put((byte) 0b10000000);
        // Step 3: Append the data with padding of multiple '0'
        // so the length would be equal to x = 448 mod 512
        buffer.put(new byte[paddingLength - 1]);
        // Step 4: Append the source data length as a 64-bit Integer.
        buffer.putLong(sourceBytes.length * 8L);

        return buffer.array();
    }

    private int calculatePaddingLength(int sourceLength) {
        // Calculate the length of the padded area
        return (512 - (sourceLength * 8 + 64) % 512) / 8;
    }

    private byte[][] breakIntoChunks(byte[] messageBlock) {
        // Prepare a two-dimensional array to store the 512-bit parts in
        byte[][] chunks = new byte[messageBlock.length / (512 / 8)][(512 / 8)];

        // Copy each 512-bit part of prepared messageBlock to two-dimensional array
        for (int i = 0; i < messageBlock.length / (512 / 8); i++) {
            System.arraycopy(
                messageBlock,   // Source array from which values are copied
                i * (512 / 8),  // Starting position in source array
                chunks[i],      // Destination array to which values are copied
                0,              // Starting position in destination array
                (512 / 8)       // Number of elements to be copied
            );
        }
        return chunks;
    }

    private int[][] createMessageSchedules(byte[][] chunks) {
        // Step 6: Create a message schedule with 64 words each
        // for every chunk, each word should be a 32-bit value
        int[][] messageSchedules = new int[chunks.length][64];

        // Step 7: For every 512-bit chunk break it into 16 32-bit words
        // and write them into first 16 entries of its message schedule
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < 16; j++) {
                // Convert every 4 bytes from byte array into single int
                messageSchedules[i][j] =
                        ((chunks[i][j * 4] & 0xFF) << 24) |
                        ((chunks[i][j * 4 + 1] & 0xFF) << 16) |
                        ((chunks[i][j * 4 + 2] & 0xFF) << 8) |
                        (chunks[i][j * 4 + 3] & 0xFF);
            }
        }
        return messageSchedules;
    }

    private int[][] computeMessageSchedules(int[][] messageSchedules) {
        // Step 8: For every message schedule compute the words
        // from 16th to 64th according to the following formula:
        //
        //     Wj = Wj-16 + σ0(Wj-15) + Wj-7 + σ1(Wj-2)
        //
        // Each value should then be computed as Wj mod 2^32 to fit into a 32-bit word
        for (int[] messageSchedule : messageSchedules) {
            for (int j = 16; j < 64; j++) {

                int w0 = messageSchedule[j - 16];
                int s0 = smallSigma0(messageSchedule[j - 15]);
                int w1 = messageSchedule[j - 7];
                int s1 = smallSigma1(messageSchedule[j - 2]);

                messageSchedule[j] = w0 + s0 + w1 + s1;
            }
        }
        return  messageSchedules;
    }

    private int smallSigma0(int x) {
        // Simple function that uses bitwise operations
        //
        // Rotate the bits to the right by 7 positions
        // Rotate the bits to the right by 18 positions
        // Right shift the bits by 3 positions
        //
        // Apply bitwise XOR to the rotated and shifted values
        return Integer.rotateRight(x,7) ^
                Integer.rotateRight(x,18) ^
                (x >>> 3);
    }

    private int smallSigma1(int x) {
        // Simple function that uses bitwise operations
        //
        // Rotate the bits to the right by 17 positions
        // Rotate the bits to the right by 19 positions
        // Right shift the bits by 10 positions
        //
        // Apply bitwise XOR to the rotated and shifted values
        return Integer.rotateRight(x,17) ^
                Integer.rotateRight(x,19) ^
                (x >>> 10);
    }

    private byte[] calculateHash(int[][] expandedMessageSchedules) {
        // Copy initial Hash values to new array
        int[] H = Arrays.copyOf(H0, H0.length);

        // Perform following computations sequentially for every expanded message schedule
        for(int[] expandedMessageSchedule : expandedMessageSchedules) {
            // Step 9: Initialize registers a, b, c, d, e, f, g, h with hash values
            // calculated from the previous message scheduler, H0 for first one
            int a = H[0], b = H[1], c = H[2], d = H[3],
                e = H[4], f = H[5], g = H[6], h = H[7];

            // Step 10: For every word in a message scheduler perform following operations:
            for (int i = 0 ;i < 64; i++) {
                int T1 = h + bigSigma1(e) + ch(e,f,g) + K[i] + expandedMessageSchedule[i];
                int T2 = bigSigma0(a) + maj(a,b,c);
                h = g; g = f; f = e; e = d + T1; d = c; c=b; b=a; a=T1+T2;
            }

            // Step 11: Compute and save hash value for current message scheduler
            H[0] += a; H[1] += b; H[2] += c; H[3] += d;
            H[4] += e; H[5] += f; H[6] += g; H[7] += h;
        }

        // Step 12: After all message schedulers have been computed the final SHA-256
        // hash value is the concatenation of the values from the H array.
        ByteBuffer byteBuffer = ByteBuffer.allocate((256/8));
        for (int value : H) byteBuffer.putInt(value);

        return byteBuffer.array();
    }

    private int bigSigma0(int x) {
        // Simple function that uses bitwise operations
        //
        // Rotate the bits to the right by 2 positions
        // Rotate the bits to the right by 13 positions
        // Rotate the bits to the right by 22 positions
        //
        // Apply bitwise XOR to the rotated and shifted values
        return Integer.rotateRight(x,2) ^
                Integer.rotateRight(x,13) ^
                Integer.rotateRight(x,22);
    }

    private int bigSigma1(int x) {
        // Simple function that uses bitwise operations
        //
        // Rotate the bits to the right by 6 positions
        // Rotate the bits to the right by 11 positions
        // Rotate the bits to the right by 25 positions
        //
        // Apply bitwise XOR to the rotated and shifted values
        return Integer.rotateRight(x,6) ^
                Integer.rotateRight(x,11) ^
                Integer.rotateRight(x,25);
    }

    private int ch(int e, int f, int g) {
        // Function "Choose"
        // Chooses a bit from values f or g based on the corresponding bit from value e
        // If the bit in value e is '0', the result bit is taken from value g if '1',
        // it is taken from value f
        return (e & f) ^ ((~e) & g);
    }

    private int maj(int a, int b, int c) {
        // Function "Majority"
        // Compares corresponding bits in values a, b, and c
        // If there are more '0' bits than '1' bits, the result bit is set to '0'
        // otherwise, it is set to '1'
        return (a & b) ^ (a & c) ^ (b & c);
    }
}
