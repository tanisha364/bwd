package com.bwd.bwd.samplecodes;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRequestToString { 

    public static void main(String[] args) {
        // Example JSON request as a string
        String jsonRequest = "{\"name\":\"John Doe\",\"age\":30,\"city\":\"Example City\"}";

        // Convert JSON request to JSON string
        String jsonString = convertJsonRequestToString(jsonRequest);
        System.out.println("JSON String: " + jsonString);
    }

    private static String convertJsonRequestToString(String jsonRequest) {
        try {
            // Create ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Read JSON string as Object
            Object jsonAsObject = objectMapper.readValue(jsonRequest, Object.class);

            // Convert Object to JSON string
            return objectMapper.writeValueAsString(jsonAsObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}