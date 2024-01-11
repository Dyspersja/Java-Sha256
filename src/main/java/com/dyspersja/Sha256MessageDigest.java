package com.dyspersja;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Sha256MessageDigest {

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

        return null;
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
}
