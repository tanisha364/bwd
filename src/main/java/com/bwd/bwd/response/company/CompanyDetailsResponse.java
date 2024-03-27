package com.bwd.bwd.response.company;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CompanyDetailsResponse {
	
	private String companydetailid;
	private String companyid;
	private String companylogo;
	private String isdefault;
	private String companydepid;
	private String companydepadd;
	private String companydepadd2;
	private String companydepcity;
	private String companydepstate;
	private String companydepzip;
	private String zone_id;
	private String companydepphone;
	private String companycontactid;
	private String country;
	
	public CompanyDetailsResponse() {
	}
	
	public CompanyDetailsResponse(String companydetailid, String companyid, String companylogo, String isdefault, String companydepid,
			String companydepadd, String companydepadd2, String companydepcity, String companydepstate,
			String companydepzip, String zone_id, String companydepphone, String companycontactid, String country) {
	
		this.companydetailid = companydetailid;
		this.companyid = companyid;
		this.companylogo = companylogo;
		this.isdefault = isdefault;
		this.companydepid = companydepid;
		this.companydepadd = companydepadd;
		this.companydepadd2 = companydepadd2;
		this.companydepcity = companydepcity;
		this.companydepstate = companydepstate;
		this.companydepzip = companydepzip;
		this.zone_id = zone_id;
		this.companydepphone = companydepphone;
		this.companycontactid = companycontactid;
		this.country = country;
	}
	
	public CompanyDetailsResponse getObject(String arr[])
	{
		this.companydetailid = arr[0];
		this.companyid = arr[1];
		this.companylogo = arr[2];
		this.isdefault = arr[3];
		this.companydepid = arr[4];
		this.companydepadd = arr[5];
		this.companydepadd2 = arr[6];
		this.companydepcity = arr[7];
		this.companydepstate = arr[8];
		this.companydepzip = arr[9];
		this.zone_id = arr[10];
		this.companydepphone = arr[11];
		this.companycontactid = arr[12];
		this.country = arr[13];
		
		return this;
	}
}
