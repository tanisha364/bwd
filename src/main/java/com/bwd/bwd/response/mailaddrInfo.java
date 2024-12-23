package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class mailaddrInfo {
	
	String usermailing1;
	String usermailing2;
	String usermailingcity;
	String usermailingstate;
	String usermailingzip;
	String usermailingcountry;
	
	public mailaddrInfo create(String usermailing1,
			String usermailing2,
			String usermailingcity,
			String usermailingstate,
			String usermailingzip, String usermailingcountry)
			{
				this.usermailing1 = usermailing1;
				this.usermailing2 = usermailing2;
				this.usermailingcity = usermailingcity;
				this.usermailingstate = usermailingstate;
				this.usermailingzip = usermailingzip;
				this.usermailingcountry = usermailingcountry;
				
				return this;
			}
}
