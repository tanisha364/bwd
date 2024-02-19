package com.bwd.bwd.serviceimpl;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bwd.bwd.model.UserAccounts;
import com.bwd.bwd.repository.UserAccountsRepo;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.response.LoginResponse;
import com.bwd.bwd.service.LoginServices;

public class LoginServicesImpl implements LoginServices	 {
	
	@Autowired
	UserAccountsRepo uar;
	
	@Autowired
	LoginResponse lr;	
	
	@Override
	public LoginResponse checkUser(LoginData ld) 
	{		
		LoginResponse lr = new LoginResponse();
		lr.setEmail(ld.getEmail());
		lr.setPassword(ld.getPassword());
		lr.setMessage("Great you can go ahead");
		lr.setUserStatus(1);
		lr.setValid(true);
		
		return lr;
	}

	@Override
	public LoginResponse checkEmail(LoginData ld) {
		LoginResponse lr = new LoginResponse();

		UserAccounts ua = (UserAccounts) uar.findByEmail(ld.getEmail());
		lr.setUserStatus(ua.getUserlevel());		
		
		return lr;
	}


	public LoginResponse checkPasswordText(LoginData ld, UserAccounts ua) {
		// TODO Auto-generated method stub		
		LoginResponse lr = new LoginResponse();
		if(ua.getPassword().equals(ld.getPassword()))
		{
			lr.setPassword(ld.getPassword());
			lr.setUserStatus(2);      // 2 -  if password matched
		}
		else
		{
			lr.setPassword(ld.getPassword());
			lr.setUserStatus(3);     // 3 -  if password not matched
		}
			
		return lr;
	}	
	
	public String getHash(String plainPassword)
	{
		 int strength = 10; // work factor of bcrypt
		 BCryptPasswordEncoder bCryptPasswordEncoder =
		  new BCryptPasswordEncoder(strength, new SecureRandom());
		 String encodedPassword = bCryptPasswordEncoder.encode(plainPassword);
		 
		 return encodedPassword;
	}
	
	public boolean comparePassword(String textPassword,String dbPassword)
	{
		boolean passChecker = false;
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		passChecker = bc.matches(textPassword,dbPassword);
		
		return passChecker;
	}	
	
	
	public static void main(String [] args)
	{
		LoginServicesImpl lsi = new LoginServicesImpl();
	}


}
