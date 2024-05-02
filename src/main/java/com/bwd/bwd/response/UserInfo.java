package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class UserInfo {
	//private Long useraccountid;
	private String firstname;
	private String lastname;		
	private BigDecimal status;
	private Timestamp statusdate;
}
