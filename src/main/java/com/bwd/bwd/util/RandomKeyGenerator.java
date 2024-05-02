package com.bwd.bwd.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomKeyGenerator {

    public static String generateRandomKey(long seed) {
        SecureRandom random = new SecureRandom();
        random.setSeed(seed);

        byte[] bytes = new byte[20]; // Adjust length to your requirement
        random.nextBytes(bytes);

        String key = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Trim to ensure 20 characters
        return key.substring(0, 20);
    }
    
    public static String generateRandomKey() {
    	long seed = System.currentTimeMillis();
        SecureRandom random = new SecureRandom();
        random.setSeed(seed);

        byte[] bytes = new byte[20]; // Adjust length to your requirement
        random.nextBytes(bytes);

        String key = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Trim to ensure 20 characters
        return key.substring(0, 20);
    }   
    
    public static String generateRandomKey(int lenght) {
    	long seed = System.currentTimeMillis();
        SecureRandom random = new SecureRandom();
        random.setSeed(seed);

        byte[] bytes = new byte[30]; // Adjust length to your requirement
        random.nextBytes(bytes);

        String key = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Trim to ensure 20 characters
        return key.substring(0, lenght);
    }       

    

    public static void main(String[] args) {
        long seed = System.currentTimeMillis(); // You can set your own seed here
//        String randomKey = generateRandomKey(seed);
//        System.out.println("Generated Random Key: " + randomKey);
//        
//        randomKey = generateRandomKey();
//        System.out.println("Generated Random Key: " + randomKey);   
        
        String randomKey = generateRandomKey(15);
        System.out.println("Generated Random Key: " + randomKey);           
    }
}