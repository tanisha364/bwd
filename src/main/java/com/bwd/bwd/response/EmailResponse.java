package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class EmailResponse {

	private StatusResponse status;		
	private String email;	
	private String phonenumber;
	private int emailId;
	private int tel_code;
}
