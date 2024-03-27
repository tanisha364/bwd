package com.bwd.bwd.response.company;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class CompanyListResponse {
	
	private String companyid;
	private String companyname;
	private String whitelabel;
	private String type;
	
	public CompanyListResponse() {
	}
	
	public CompanyListResponse(String companyid, String companyname, String whitelabel, String type) {
		this.companyid = companyid;
		this.companyname = companyname;
		this.whitelabel = whitelabel;
		this.type = type;
	}
	
	public CompanyListResponse getObject(String arr[])
	{
		this.companyid = arr[0];
		this.companyname = arr[1];
		this.whitelabel = arr[2];
		this.type = arr[3];
		
		return this;
	}
	
	
}
