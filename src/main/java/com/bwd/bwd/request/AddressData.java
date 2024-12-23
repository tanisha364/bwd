package com.bwd.bwd.request;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user_details")
@Entity
@Getter
@Setter
public class AddressData{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userdetailid;
	
	private int useraccountid;
	private String useraddress1;
	private String useraddress2;
	private String usercity;
	private String userstate;
	private String userzip;
	private String usercountry;
	
	private int usermailingsame;
	
	private String usermailing1;
	private String usermailing2;
	private String usermailingcity;
	private String usermailingstate;
	private String usermailingzip;
	private String usermailingcountry;
	
	public AddressData createAdd(int id, String useraddress1, String useraddress2, String usercity, String userstate, String userzip,
			String usercountry,  int usermailingsame, String usermailing1, String usermailing2,String usermailingcity,
			String usermailingstate,String usermailingzip,String usermailingcountry)
	{		
		this.useraccountid = id;
		this.useraddress1 = useraddress1;
		this.useraddress2 = useraddress2;
		this.usercity = usercity;
		this.userstate = userstate;
		this.userzip = userzip;
		this.usercountry = usercountry;
		this.usermailingsame = usermailingsame;
		this.usermailing1 = usermailing1;
		this.usermailing2 = usermailing2;
		this.usermailingcity = usermailingcity;
		this.usermailingstate = usermailingstate;
		this.usermailingzip = usermailingzip;
		this.usermailingcountry = usermailingcountry;
		
		return this;
	}   
}
