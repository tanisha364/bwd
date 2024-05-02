package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class ReportInfo {
	private int rep_report_id;
	private String rep_repor_name;
	private String rep_report_description;
	private String rep_report_type;
	private int categoryid;
}
