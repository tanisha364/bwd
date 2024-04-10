package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class RegTextResponse {

   private String text;
   private String logo;
	
	public RegTextResponse()
	{
		
	}
	
	public RegTextResponse(String text, String logo) {
		super();
		this.text = text;
		this.logo = logo;
	}
	
	public RegTextResponse getObject(String arr[])
	{
		this.logo = arr[0];	
		this.text = arr[1];
		return this;
	}
}
