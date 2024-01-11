package com.dyspersja;

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
}
