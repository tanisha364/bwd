package com.bwd.bwd.service.company;

import java.util.List;

import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.company.CompanyDetailsResponse;
import com.bwd.bwd.response.company.CompanyListResponse;


public interface CompanyServices {
	public List<CompanyListResponse> findCompanyList(UserData requestData);

	public List<CompanyDetailsResponse> findCompanyDetaist(UserData requestData);
	
}
