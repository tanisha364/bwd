package com.bwd.bwd.model.jobsmith;

import java.beans.JavaBean;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class Reports {
	@Column(name = "Rep_ReportId")
	private int Rep_ReportId;
	
	@Column(name = "Rep_ReportName")
	private String Rep_ReportName;
	
	@Column(name = "Rep_ReportDescription")
	private String Rep_ReportDescription;
	
	@Column(name = "Rep_ReportType")
	private String Rep_ReportType;
	
	@Column(name = "Rep_Deleted")
	private byte Rep_Deleted;
	
	@Column(name = "Rep_ReportIntroduction")
	private String Rep_ReportIntroduction;
	
	@Column(name = "Rep_ReportCode")
	private String Rep_ReportCode;
	
	@Column(name = "Rep_PersonalityViewable")
	private int Rep_PersonalityViewable;
	
	public Reports()
	{
		
	}

	public Reports(int Rep_ReportId_,String Rep_ReportName_,String Rep_ReportDescription_,String Rep_ReportType_,byte Rep_Deleted_,String Rep_ReportIntroduction_,String Rep_ReportCode_,int Rep_PersonalityViewable_)
	{
		this.Rep_ReportId = Rep_ReportId_;
		this.Rep_ReportName= Rep_ReportName_;
		this.Rep_ReportDescription = Rep_ReportDescription_;
		this.Rep_ReportType = Rep_ReportType_;
		this.Rep_Deleted = Rep_Deleted_;
		this.Rep_ReportIntroduction = Rep_ReportIntroduction_;
		this.Rep_ReportCode = Rep_ReportCode_;
		this.Rep_PersonalityViewable = Rep_PersonalityViewable_;
	}
	
	public Reports getObject(String data[]) {
		
		this.Rep_ReportId = Integer.parseInt(data[0]);
		this.Rep_ReportName= data[1];
		this.Rep_ReportDescription = data[2];
		this.Rep_ReportType = data[3];
		this.Rep_Deleted = Byte.parseByte(data[4]);
		this.Rep_ReportIntroduction = data[5];
		this.Rep_ReportCode = data[6];
		this.Rep_PersonalityViewable = Integer.parseInt(data[7]);
		
		return this;
	}
}









