package com.bwd.bwd.response.company;

import com.bwd.bwd.response.StatusResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCompany{
	
	private StatusResponse status;
	private CompanyResponse data;
}