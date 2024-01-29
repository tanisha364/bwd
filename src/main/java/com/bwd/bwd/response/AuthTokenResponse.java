package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class AuthTokenResponse 
{
	private StatusResponse status;	
	
	private AuthResponse authData;	
	//	private TokenResponse tokenData;
}
