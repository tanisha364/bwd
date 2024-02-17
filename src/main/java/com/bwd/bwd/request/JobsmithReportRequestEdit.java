package com.bwd.bwd.request;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class JobsmithReportRequestEdit {
	public int jobsmith_report_capabilityid;
	public int jobsmith_reportid;
	public String capabilityid;
	public String weightage;
	public String sequence;
	
	public JobsmithReportRequestEdit()
	{
		
	}

	public JobsmithReportRequestEdit(int jobsmith_report_capabilityid, int jobsmith_reportid, String capabilityid,
			String weightage, String sequence) {
		super();
		this.jobsmith_report_capabilityid = jobsmith_report_capabilityid;
		this.jobsmith_reportid = jobsmith_reportid;
		this.capabilityid = capabilityid;
		this.weightage = weightage;
		this.sequence = sequence;
	}
	
	
	public JobsmithReportRequestEdit createObject(int jobsmith_report_capabilityid, int jobsmith_reportid, String capabilityid,
			String weightage, String sequence) {
		this.jobsmith_report_capabilityid = jobsmith_report_capabilityid;
		this.jobsmith_reportid = jobsmith_reportid;
		this.capabilityid = capabilityid;
		this.weightage = weightage;
		this.sequence = sequence;
		
		return this;
	} 
	
}
