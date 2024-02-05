package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class UserInfo {
	private int useraccountid;
	private String firstname;
	private String lastname;		
	private int status;
	private Date statusdate;
}
