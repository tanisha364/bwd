package com.bwd.bwd.model.jobsmith;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class Critical {
	public String capabilityid;
	public String weightage;
	public String sequence;
	private int editstatus; 
	private int jobsmith_report_capabilityid;	
}