package com.bwd.bwd.samplecodes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class UniqueKeyGenerator {

    public static String generateUniqueKey(long userId) {
        try {
            // Convert the long to bytes
            byte[] userIdBytes = BigInteger.valueOf(userId).toByteArray();

            // Get a MessageDigest instance with the desired algorithm (e.g., SHA-256)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the digest with the user ID bytes
            byte[] hashedBytes = digest.digest(userIdBytes);

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
        String uniqueKey = generateUniqueKey(userId);
        System.out.println("Generated Unique Key: " + uniqueKey);
    }
//39ce42527476bb7587e87838126bbf616def30a999957758c91ed3393c06b0aa   
//39ce42527476bb7587e87838126bbf616def30a999957758c91ed3393c06b0aa    
}