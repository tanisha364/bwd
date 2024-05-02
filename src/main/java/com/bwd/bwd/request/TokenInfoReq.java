package com.bwd.bwd.request;

import java.beans.JavaBean;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class TokenInfoReq 
{	private String client_id;
	private String client_secret;
	private String authToken;
	private Date timestamp;	
	//private Date expires_in;
	private long exp;
	private String token_type;
}
