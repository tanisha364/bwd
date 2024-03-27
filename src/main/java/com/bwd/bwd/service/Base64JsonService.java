package com.bwd.bwd.service;

public interface Base64JsonService {
	public String base64EncodeJson(String jsonString);
	public String base64DecodeJson(String encodedJson);
	public String convertJsonRequestToString(String jsonRequest);
}
