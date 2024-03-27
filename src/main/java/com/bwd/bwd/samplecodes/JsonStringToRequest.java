package com.bwd.bwd.samplecodes;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

public class JsonStringToRequest {

    public static void main(String[] args) {
        // Example JSON string
        String jsonString = "{\"stuName\":\"John Doe\",\"age\":30,\"city\":\"Example City\"}";

        // Convert JSON string to Java object
        MyRequestObject requestObject = convertJsonStringToRequest(jsonString);
        System.out.println("Converted Java Object: " + requestObject);
    }

    private static MyRequestObject convertJsonStringToRequest(String jsonString) {
        try {
            // Create ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert JSON string to Java object
            return objectMapper.readValue(jsonString, MyRequestObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Example class representing the structure of your JSON request
@Getter
@Setter
class MyRequestObject {
    private String stuName;
    private int age;
    private String city;

    // Add getters and setters

    @Override
    public String toString() {
        return "MyRequestObject{" +
                "stuName='" + stuName + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                '}';
    }
}