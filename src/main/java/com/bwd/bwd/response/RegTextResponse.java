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
   private String video;
   private String image;
	
	public RegTextResponse()
	{
		
	}
	
	public RegTextResponse(String text, String logo, String companyname, String DefaultRegistrationVideo, String video, String photo) {
		super();
		this.text = text;
		this.logo = logo;
		this.companyname = companyname;
		this.video = video;
		this.image = photo;
	}
	
	public RegTextResponse getObject(String arr[])
	{
		this.logo = arr[0];	
		this.text = arr[1];
		this.companyname = arr[2];
		this.video = arr[3];
		this.image = arr[4];
		
		return this;
	}
}