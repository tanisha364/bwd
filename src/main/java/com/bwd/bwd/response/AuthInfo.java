package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class AuthInfo {
	private int useraccountid;
	private String email;	
	private String regnum;
	private int userLevel;	
	private String userid;
	private String refreshtoken;
	private String firstname;	
	private String lastname;
}
