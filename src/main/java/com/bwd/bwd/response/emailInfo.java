package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class emailInfo {

	long bwd_email_id;
	String email;
	int primary;
	String type;
	int type_id;
	
	public emailInfo create(long bwd_email_id, String email, int primary, String type, int type_id)
	{
		this.bwd_email_id = bwd_email_id;
		this.email = email;
		this.primary = primary;
		this.type = type;
		this.type_id = type_id;
		return this;
	}
}
