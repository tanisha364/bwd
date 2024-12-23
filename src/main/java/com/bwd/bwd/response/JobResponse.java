package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class JobResponse {

	private StatusResponse status;
	private List<Job> data;
	
}
