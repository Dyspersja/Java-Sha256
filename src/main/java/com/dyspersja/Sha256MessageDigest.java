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
        return null;
    }

    private byte[] createMessageBlock(byte[] sourceBytes) {
        int paddingLength = calculatePaddingLength(sourceBytes);
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

    private int calculatePaddingLength(byte[] sourceBytes) {
        // Calculate the length of the padded area
        return (512 - (sourceBytes.length * 8 + 64) % 512) / 8;
    }
}
