package com.bwd.bwd.response;


import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class PreMadeReport {
	private String jobsmithPremadeReportid; 
	private String repReporName;
	private String jobsmithCategoryid;
	
	public  PreMadeReport() {}
	
	public PreMadeReport(String jobsmithPremadeReportid, String repReporName, String jobsmithCategoryid) {
		super();
		this.jobsmithPremadeReportid = jobsmithPremadeReportid;
		this.repReporName = repReporName;
		this.jobsmithCategoryid = jobsmithCategoryid;
	}
	
	public  PreMadeReport getObject(String arr[]) 
	{
		this.jobsmithPremadeReportid = arr[0];
		this.repReporName = arr[1];
		this.jobsmithCategoryid = arr[2];
		
		return this;
	}


}
