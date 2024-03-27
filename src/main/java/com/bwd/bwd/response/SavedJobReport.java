package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class SavedJobReport{
	private String jobsmithReportId; 
	private String jobsmithReportName;
	private String reportStatus;
	private String createdBy;
	private String dateModifed; 
	private String locked;

	public SavedJobReport()
	{
		
	}
	
	public SavedJobReport(String jobsmithReportId, String jobsmithReportName, String reportStatus, String createdBy,
			String dateModifed, String locked) {
		super();
		this.jobsmithReportId = jobsmithReportId;
		this.jobsmithReportName = jobsmithReportName;
		this.reportStatus = reportStatus;
		this.createdBy = createdBy;
		this.dateModifed = dateModifed;
		this.locked = locked;
	}
	
	public SavedJobReport getObject(String arr[])
	{
		this.jobsmithReportId = arr[0];
		this.jobsmithReportName = arr[1];
		this.reportStatus = arr[2];
		this.createdBy = arr[3];
		this.dateModifed = arr[4];
		this.locked = arr[5];
		
		return this;
	}
}