package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;


@JavaBean
@Getter
@Setter
public class SavedJobReportsExpansion {
	private String jobsmithRreportId; 
	private String jobsmith_report_note;
	private String capabilityid;
	private String cap_Capability; 
	private String weightage;
	private String report_status; 
	private String status_date;
	private String sequence;
	
	public SavedJobReportsExpansion()
	{
		
	}
	
	public SavedJobReportsExpansion(String jobsmithRreportId, String jobsmith_report_note,  String capabilityid, String cap_Capability,
			String weightage, String report_status, String status_date, String sequence) {
		super();
		this.jobsmithRreportId = jobsmithRreportId;
		this.jobsmith_report_note = jobsmith_report_note;
		this.capabilityid = capabilityid;
		this.cap_Capability = cap_Capability;
		this.weightage = weightage;
		this.report_status = report_status;
		this.status_date = status_date;
		this.sequence = sequence;
	}
	
	public SavedJobReportsExpansion getObject(String arr[])
	{
		this.jobsmithRreportId = arr[0];
		this.jobsmith_report_note = arr[1];
		this.capabilityid = arr[2];
		this.cap_Capability = arr[3];
		this.weightage = arr[4];
		this.report_status = arr[5];
		this.status_date = arr[6];
		this.sequence = arr[7];
		
		return this;
	}	
}