package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter

public class PreMadeReportResponse {
	private List<PreMadeReport> preMadeReport;
}
