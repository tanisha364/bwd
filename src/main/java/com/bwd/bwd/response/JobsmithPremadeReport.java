package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class JobsmithPremadeReport 
{
	String Jobsmith_premade_reportid;
	String Rep_repor_name;
	String Jobsmith_categoryid;
	String Jobsmith_category;
	String Rep_Cap_Capability_Id;
	String Cap_Capability;
	String Rep_Cap_Weightage;
	String Rep_Cap_Sequence;
	
	public JobsmithPremadeReport() {}
	
	public JobsmithPremadeReport(String jobsmith_premade_reportid, String rep_repor_name,
			String jobsmith_categoryid, String jobsmith_category, String rep_Cap_Capability_Id, String cap_Capability,
			String rep_Cap_Weightage, String rep_Cap_Sequence) {
		super();
		Jobsmith_premade_reportid = jobsmith_premade_reportid;
		Rep_repor_name = rep_repor_name;
		Jobsmith_categoryid = jobsmith_categoryid;
		Jobsmith_category = jobsmith_category;
		Rep_Cap_Capability_Id = rep_Cap_Capability_Id;
		Cap_Capability = cap_Capability;
		Rep_Cap_Weightage = rep_Cap_Weightage;
		Rep_Cap_Sequence = rep_Cap_Sequence;
	}
	
	public JobsmithPremadeReport getObject(String arr[])
	{
		this.Jobsmith_premade_reportid = arr[0];
		this.Rep_repor_name = arr[1];
		this.Jobsmith_categoryid = arr[2];
		this.Jobsmith_category = arr[3];
		this.Rep_Cap_Capability_Id = arr[4];
		this.Cap_Capability = arr[5];
		this.Rep_Cap_Weightage = arr[6];
		this.Rep_Cap_Sequence = arr[7];
		
		return this;
	}
}