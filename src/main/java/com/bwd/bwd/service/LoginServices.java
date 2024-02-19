package com.bwd.bwd.service;


import org.springframework.stereotype.Service;

import com.bwd.bwd.model.UserAccounts;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.response.LoginResponse;

@Service
public interface LoginServices 
{

	
	public  LoginResponse checkUser(LoginData ld);
	
	public LoginResponse checkEmail(LoginData ld);	
	
}
