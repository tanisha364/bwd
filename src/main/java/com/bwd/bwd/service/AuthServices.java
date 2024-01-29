package com.bwd.bwd.service;

import org.springframework.stereotype.Service;

import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.response.AuthResponse;

@Service
public interface AuthServices {
	
	public AuthResponse authenticateUser(LoginData ld);
	
	public AuthResponse checkUser(UserAccountsAuth uaa, AuthResponse ar);
	
	public AuthResponse checkEmail(LoginData ld);	
	
	public AuthResponse checkLevel(int userlevel, AuthResponse ar);
	
	public AuthResponse checkPassword(LoginData ld,UserAccountsAuth uaa, AuthResponse ar);		
}