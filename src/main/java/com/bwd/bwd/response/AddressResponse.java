package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class AddressResponse {

	private StatusResponse status;		
	private List<countryInfo> countries;	
	private List<cityInfo> cities;	
	
}
