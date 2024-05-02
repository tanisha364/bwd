package com.bwd.bwd.response;

import java.beans.JavaBean;

import com.bwd.bwd.model.jobsmith.JobSearchResult;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class JobTitleResponse {
	private int jobsmith_reportid;
	private String jobsmith_report_name;
	private int useraccountid;
	private String username;
	private String lastmodified;

	public JobTitleResponse() {

	}
	
	@Override
	public String toString() {
		return "JobTitleResponse [jobsmith_reportid=" + jobsmith_reportid + ", jobsmith_report_name="
				+ jobsmith_report_name + ", useraccountid=" + useraccountid + ", username=" + username
				+ ", lastmodified=" + lastmodified + "]";
	}

	public JobTitleResponse(String data[]) {
		this.jobsmith_reportid = Integer.parseInt(data[0]);;
		this.jobsmith_report_name = data[1];
		this.useraccountid = Integer.parseInt(data[2]);
		this.username = data[3];
		this.lastmodified = data[3];		
	}

	public JobTitleResponse getObject(String data[]) 
	{
		this.jobsmith_reportid = Integer.parseInt(data[0]);;
		this.jobsmith_report_name = data[1];
		this.useraccountid = Integer.parseInt(data[2]);
		this.username = data[3];
		this.lastmodified = data[4];	
		
	//	System.out.println(this);
		
		return this;
	}
}