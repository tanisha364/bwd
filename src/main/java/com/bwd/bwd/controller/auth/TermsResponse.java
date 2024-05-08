package com.bwd.bwd.controller.auth;

import java.beans.JavaBean;

import com.bwd.bwd.response.RegPageResponse;
import com.bwd.bwd.response.StatusResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class TermsResponse {
	private StatusResponse status;		
	private  String email;	
}
