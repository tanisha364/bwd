package com.bwd.bwd.response;
import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class Testinfo {

	int landingassessmentid;
	String description;
	
	public Testinfo create(int landingassessmentid, String description)
	{
		this.landingassessmentid = landingassessmentid;
		this.description = description;
		return this;
	}
	
}
