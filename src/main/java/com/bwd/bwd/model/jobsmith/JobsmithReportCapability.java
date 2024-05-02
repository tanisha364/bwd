package com.bwd.bwd.model.jobsmith;

import com.bwd.bwd.request.JobsmithReportRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "jobsmith_report_capability_tbl")
@Entity
@Setter
@Getter
public class JobsmithReportCapability
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "jobsmith_report_capabilityid")
	public int jobsmith_report_capabilityid;
	public int jobsmith_reportid;
	public int capabilityid;
	public String weightage;
	public int sequence;
	
	public JobsmithReportCapability()
	{
		
	}

	public JobsmithReportCapability(int jobsmith_report_capabilityid_,int jobsmith_reportid_,int capabilityid_,String weightage_,int sequence_)
	{
		this.jobsmith_report_capabilityid = jobsmith_report_capabilityid_;
		this.jobsmith_reportid = jobsmith_reportid_;
		this.capabilityid = capabilityid_;
		this.weightage = weightage_;
		this.sequence = sequence_;
	}
	
	public JobsmithReportCapability createJobsmithReportCapability( int id, int capabilityid,String weightage,int sequence)
	{
		this.jobsmith_reportid = id;
		this.capabilityid = capabilityid;
		this.weightage = weightage;
		this.sequence = sequence;
		
		return this;
	}
}
