package com.bwd.bwd.model.jobsmith;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "reports_tbl")
@Entity
@Getter
@Setter
public class Reports {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "rep_report_id")
	private int rep_report_id;
	
	@Column(name = "rep_repor_name")
	private String rep_repor_name;
	
	@Column(name = "rep_report_description")
	private String rep_report_description;
	
	@Column(name = "rep_report_type")
	private String rep_report_type;
	
	@Column(name = "rep_deleted")
	private byte rep_deleted;
	
	@Column(name = "rep_report_introduction")
	private String rep_report_introduction;
	
	@Column(name = "rep_report_code")
	private String rep_report_code;
	
	@Column(name = "rep_personality_viewable")
	private int rep_personality_viewable;
	
//	private long categoryid;
	public Reports()
	{
		
	}

	public Reports(int Rep_ReportId_,String Rep_ReportName_,String Rep_ReportDescription_,String Rep_ReportType_,byte Rep_Deleted_,String Rep_ReportIntroduction_,String Rep_ReportCode_,int Rep_PersonalityViewable_)
	{
		this.rep_report_id = Rep_ReportId_;
		this.rep_repor_name= Rep_ReportName_;
		this.rep_report_description = Rep_ReportDescription_;
		this.rep_report_type = Rep_ReportType_;
		this.rep_deleted = Rep_Deleted_;
		this.rep_report_introduction = Rep_ReportIntroduction_;
		this.rep_report_code = Rep_ReportCode_;
		this.rep_personality_viewable = Rep_PersonalityViewable_;
	}
}