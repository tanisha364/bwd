package com.bwd.bwd.serviceimpl;

import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.UserDataResponse;
import com.bwd.bwd.response.UserInfo;
import com.bwd.bwd.response.UserInfoResponse;
import com.bwd.bwd.service.UserInfoService;

public class UserInfoImpl implements UserInfoService
{

	@Override
	public UserInfoResponse checkUser(UserAccountsAuth uaa, UserInfoResponse uir) {
		
		UserInfo ui = new UserInfo();
		StatusResponse sr = new StatusResponse();
		UserDataResponse udr = new UserDataResponse();
		try
		{
			ui.setFirstname(uaa.getFirstname());
			ui.setLastname(uaa.getLastname());
			ui.setStatus(uaa.getStatus());
			ui.setStatusdate(uaa.getStatusdate());			
			
			sr.setValid(true);
			sr.setStatusCode(1);
			sr.setMessage("Welcome !!");
			
			udr.setUserinfo(ui);
			
			uir.setStatus(sr);			
			uir.setData(udr);
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			sr.setValid(false);
			sr.setStatusCode(9);
			
			udr.setUserinfo(ui);
			
			uir.setStatus(sr);			
			uir.setData(udr);;
			sr.setMessage("Exception Occurred !!");	
		}
		return uir;
	}

}
