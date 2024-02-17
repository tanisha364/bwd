package com.bwd.bwd.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
	
	private long useraccountid;
	private String userid;
	private int categoryId;
	private int reportId;
	private String findValue;
	private int archived;
	private String report_status;
	private int locked;	
	private String jobsmithReportName;	
}
