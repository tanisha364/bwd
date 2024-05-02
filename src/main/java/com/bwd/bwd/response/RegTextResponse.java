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
   private String companyname;
	
	public RegTextResponse()
	{
		
	}
	
	public RegTextResponse(String text, String logo, String companyname) {
		super();
		this.text = text;
		this.logo = logo;
		this.companyname = companyname;
	}
	
	public RegTextResponse getObject(String arr[])
	{
		this.logo = arr[0];	
		this.text = arr[1];
		this.companyname = arr[2];
		return this;
	}
}