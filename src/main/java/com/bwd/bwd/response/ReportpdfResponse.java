package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class ReportpdfResponse {
	private int ReportId;
	private String Report_Name;
	private String Report_Type;
	private String Report_Introduction;
	private String Report_Disclaimer ;
	private String QuickScreen_Intro;
	private String StrengthChart_Intro;
	private String Report_code;
	private String User_Name;
	private List<PdfReport> Report;
}
