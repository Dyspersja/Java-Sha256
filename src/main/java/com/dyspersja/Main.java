package com.dyspersja;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main( String[] args ) throws IOException {
        // Example with byte[] input (e.g., file content)
//        byte[] sourceData = Files.readAllBytes(Paths.get("D:\\file"));

        // Example with String input
        String sourceData = "Hello World!";

        Sha256MessageDigest messageDigest = new Sha256MessageDigest();

        byte[] hash = messageDigest.digest(sourceData);
        for (byte b : hash) System.out.printf("%02x", b & 0xFF);
    }
}
