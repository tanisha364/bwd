package com.bwd.bwd.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportInfoResponse {
	private StatusResponse status;
	private ReportResponse data;
}
