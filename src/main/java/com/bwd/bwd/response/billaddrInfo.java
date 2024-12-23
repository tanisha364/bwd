package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class billaddrInfo {

	String useraddress1;
	String useraddress2;
	String usercity;
	String userstate;
	String userzip;
	String usercountry;
	
	public billaddrInfo create(String useraddress1,
			String useraddress2,
			String usercity,
			String userstate,
			String userzip, String usercountry)
			{
				this.useraddress1 = useraddress1;
				this.useraddress2 = useraddress2;
				this.usercity = usercity;
				this.userstate = userstate;
				this.userzip = userzip;
				this.usercountry = usercountry;
				
				return this;
			}
}
