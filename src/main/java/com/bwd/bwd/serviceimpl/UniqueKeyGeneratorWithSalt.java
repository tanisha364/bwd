package com.bwd.bwd.serviceimpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.math.BigInteger;

public class UniqueKeyGeneratorWithSalt {

    public static String generateUniqueKey(long userId, String salt) {
        try {
            // Combine the user ID and salt
            String combinedInput = userId + salt;

            // Convert the combined input to bytes
            byte[] inputBytes = combinedInput.getBytes();

            // Get a MessageDigest instance with the desired algorithm (e.g., SHA-256)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the digest with the combined input bytes
            byte[] hashedBytes = digest.digest(inputBytes);

            // Convert the hashed bytes to a hexadecimal string
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception (e.g., log or throw a runtime exception)
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to convert bytes to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b & 0xff));
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        long userId = 211804L;

        // Generate a random salt
        String salt = generateRandomSalt();

        String uniqueKey = generateUniqueKey(userId, salt);
        System.out.println("Generated Unique Key: " + uniqueKey);
    }

    // Helper method to generate a random salt
//    private static String generateRandomSalt() {
    public static String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // Adjust the size based on your security requirements
        random.nextBytes(saltBytes);
        return new BigInteger(1, saltBytes).toString(16);
    }
    
//d63d84c45bca459dd0cdb5f1c0e2eb187dce2d2fbaa32896cc6546fc860ec775
//74e0a81bcade76d4dbf389f7c561063af085b5d630d6f9a28e735eea781e950b    
//62e61ad7ce2caf63600ba432346cf575f674da970dc589e93745b91e163136a8    
}
