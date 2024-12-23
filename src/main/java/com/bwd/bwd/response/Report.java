package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class Report {
   
	String ReportName;
	int ReportId;
	
	public Report() {

	}
	
	public Report getObject(String arr[]) 
	{
		this.ReportName = arr[0];
		this.ReportId = Integer.parseInt(arr[1]);
		
		return this;
	}

}
