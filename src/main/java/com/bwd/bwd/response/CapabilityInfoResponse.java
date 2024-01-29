package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class CapabilityInfoResponse {
	private StatusResponse status;
	private CapabilityResponse data;
}