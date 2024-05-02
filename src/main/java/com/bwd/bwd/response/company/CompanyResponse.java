package com.bwd.bwd.response.company;

import java.beans.JavaBean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class CompanyResponse  {
	private List<CompanyListResponse> CompanyListResponse;
	
}