package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class LoginResponse 
{
	private Long useraccount;
	private String email;	
	private String regnum;
	private boolean isValid;
	private int userStatus;
	private int userLevel;
	private String message;
	private String password;
}
