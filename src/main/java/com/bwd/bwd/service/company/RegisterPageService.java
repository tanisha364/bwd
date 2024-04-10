package com.bwd.bwd.service.company;

import java.util.List;

import com.bwd.bwd.model.auth.AccountRequest;
import com.bwd.bwd.response.RegTextResponse;

public interface RegisterPageService {

	public List<RegTextResponse> getPageReport(AccountRequest userAccount);
	
}
