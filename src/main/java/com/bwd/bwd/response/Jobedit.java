package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean

public class Jobedit {

	public String jobsmith_report_capabilityid;
	public String report_CapabilityId;
	public String report_Weightage;
	public String cap_Capability;
	public String repCap_Sequence;

	public Jobedit() {

	}

	public Jobedit getObject(String arr[]) {
		this.jobsmith_report_capabilityid = arr[0];
		this.report_CapabilityId = arr[1];
		this.report_Weightage = arr[2];
		this.cap_Capability = arr[3];
		this.repCap_Sequence = arr[4];

		return this;
	}
}
