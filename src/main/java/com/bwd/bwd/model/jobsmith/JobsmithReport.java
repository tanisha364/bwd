package com.bwd.bwd.model.jobsmith;

import com.bwd.bwd.request.JobsmithReportRequest;
import com.bwd.bwd.util.DateTimeCreation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "jobsmith_report_tbl")
@Entity
@Setter
@Getter
public class JobsmithReport 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "jobsmith_reportid")	
	public int jobsmith_reportid;
	public String jobsmith_report_name;
	public String jobsmith_report_note;
	public int companyid;
	public int useraccountid;
	public java.sql.Timestamp date_added;
	public java.sql.Timestamp date_modifed;
	public String report_status;
	public java.sql.Timestamp status_date;
	public int locked;
	public int archived;
	
	public JobsmithReport()
	{
		
	}

	public JobsmithReport createJobsmithReport(JobsmithReportRequest dataJRR)
	{		
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
		
		this.jobsmith_report_name = dataJRR.getJobsmith_report_name();
		this.jobsmith_report_note = dataJRR.getJobsmith_report_note();
		this.companyid = -1;
		this.useraccountid = dataJRR.getUseraccountid();
		this.date_added = (java.sql.Timestamp) dt;
		this.date_modifed = (java.sql.Timestamp)dt;
		this.report_status = "Draft";
		this.status_date = (java.sql.Timestamp)dt;
		this.locked = 0;
		this.archived = 0;
		
		return this;
	}	
	
	public String toString()
	{
		String obj = this.jobsmith_report_name + "\n\t"
					 + this.jobsmith_report_note + "\n\t"
					 + this.companyid + "\n\t"
					 + this.useraccountid +"\n\t"
					 + this.date_added + "\n\t"
					 + this.date_modifed + "\n\t"
					 + this.report_status + "\n\t"
					 + this.status_date + "\n\t"
					 + this.locked + "\n\t"
					 + this.archived + "\n\t";		
		
		return obj;
	}
	
	public JobsmithReport(int jobsmith_reportid_,String jobsmith_report_name_,String jobsmith_report_note_,int companyid_,int useraccountid_,java.sql.Timestamp date_added_,java.sql.Timestamp date_modifed_,String report_status_,java.sql.Timestamp status_date_,int locked_,int archived_)
	{
		this.jobsmith_reportid = jobsmith_reportid_;
		this.jobsmith_report_name = jobsmith_report_name_;
		this.jobsmith_report_note = jobsmith_report_note_;
		this.companyid = companyid_;
		this.useraccountid = useraccountid_;
		this.date_added = date_added_;
		this.date_modifed = date_modifed_;
		this.report_status = report_status_;
		this.status_date = status_date_;
		this.locked = locked_;
		this.archived = archived_;
	}
}