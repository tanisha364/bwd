package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class ResponseReport {
	private StatusResponse status;
	private ReportpdfResponse data;
}
