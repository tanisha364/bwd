package com.bwd.bwd.service;

import org.springframework.stereotype.Service;

import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.response.UserInfoResponse;

@Service
public interface UserInfoService {
	
	public UserInfoResponse checkUser(UserAccountsAuth uaa,UserInfoResponse uir);

}
