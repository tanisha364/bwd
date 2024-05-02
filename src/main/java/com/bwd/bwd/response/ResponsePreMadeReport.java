package com.bwd.bwd.response;
import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class ResponsePreMadeReport {
	private StatusResponse status;
	private PreMadeReportResponse data;
}
