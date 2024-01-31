package com.bwd.bwd.model.jobsmith;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class JobSearchResult{
	
//	private String jobsmith_reportid;
	private String jobsmith_report_name;
	
	public JobSearchResult() {}

	public JobSearchResult getObject (String data[]) 
	{
//		jobsmith_reportid = data[0];
		jobsmith_report_name = data[0];
		
	//	System.out.println(this);
		
		return this;
	}

	@Override
	public String toString() {
		return "JobSearchResult [jobsmith_report_name="
				+ jobsmith_report_name + "]";
	}
}