package com.bwd.bwd.serviceimpl;

import com.bwd.bwd.model.auth.OauthClients;
import com.bwd.bwd.service.Base64JsonService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class Base64JsonServiceImpl implements Base64JsonService 
{
    public String base64EncodeJson(String jsonString) {
        // Encode the JSON string to bytes
        byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
        
        // Encode the bytes to Base64
        byte[] encodedBytes = Base64.getEncoder().encode(jsonBytes);

        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public String base64DecodeJson(String encodedJson) {
        // Decode the Base64 string to bytes
        byte[] decodedBytes = Base64.getDecoder().decode(encodedJson.getBytes(StandardCharsets.UTF_8));

        return new String(decodedBytes, StandardCharsets.UTF_8);
    }	
    
    public String convertJsonRequestToString(String jsonRequest) {
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
    
    public OauthClients getObject(String name) 
    {
    	OauthClients oc = null;
    	JSONObject json = null;
    	try {
    		json = new JSONObject(name);	
    		String pubicKey = "";
    		oc = new OauthClients();
    		oc.setValues((String) json.get("client_id"),(String) json.get("client_secret"), pubicKey);
    	}catch(JSONException jsone) {
    		System.out.println(jsone);
    	}    	  
    	
    	return oc;
    }    
    
    public static void main(String[] args) {
    	
    	Base64JsonServiceImpl bjsi = new Base64JsonServiceImpl();
        // Creating a JSON object
        String jsonString = "{\"name\":\"John Doe\",\"age\":30}";
        
        // Encoding the JSON object to Base64
        String encodedJson = bjsi.base64EncodeJson(jsonString);
        System.out.println("Encoded JSON: " + encodedJson);

        // Decoding the JSON object from Base64
        String decodedJson = bjsi.base64DecodeJson(encodedJson);
        System.out.println("Decoded JSON: " + decodedJson);
    }

}