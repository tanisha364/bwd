package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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
	private int progress_bar;
	private String css_color;
	private String status_help;
	private Timestamp statusdate;
	private String tel;
	private String statusDesc;
	private List<emailInfo> Emails;	
	private addressInfo Address;	
}
