package com.bwd.bwd.samplecodes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Example {

    public static void main(String[] args) {
        // String to be encoded
        String originalString = "Hello, Base64 Encoding and Decoding!";

        // Encoding
        String encodedString = base64Encode(originalString);
        System.out.println("Encoded String: " + encodedString);

        // Decoding
        String decodedString = base64Decode(encodedString);
        System.out.println("Decoded String: " + decodedString);
    }

    private static String base64Encode(String originalString) {
        // Encode the original string
        byte[] encodedBytes = Base64.getEncoder().encode(originalString.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    private static String base64Decode(String encodedString) {
        // Decode the encoded string
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString.getBytes(StandardCharsets.UTF_8));
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
