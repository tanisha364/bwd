package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class TermsResponse {
	private StatusResponse status;		
	private  String email;	
}
