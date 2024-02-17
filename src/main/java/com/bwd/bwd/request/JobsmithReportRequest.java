package com.bwd.bwd.request;

import java.beans.JavaBean;
import java.util.ArrayList;

import com.bwd.bwd.model.jobsmith.Critical;
import com.bwd.bwd.model.jobsmith.Important;
import com.bwd.bwd.model.jobsmith.Nicetohave;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class JobsmithReportRequest {
	public int jobsmith_reportid;
	public String jobsmith_report_name;
	public String jobsmith_report_note;
	public int companyid;
	private int useraccountid;
	private String userid;
	
	public ArrayList<Critical> critical;
	public ArrayList<Important> important;
	public ArrayList<Nicetohave> nicetohave;		
}