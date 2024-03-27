package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class StatusResponse {
	private boolean valid;
	private int statusCode;
	private String message;	

}
