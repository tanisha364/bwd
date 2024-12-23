package com.bwd.bwd.serviceimpl;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.response.LoginResponse;
import com.bwd.bwd.service.LoginServices;

public class LoginServicesImpl implements LoginServices	 {
	
	
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

	@Override
	public LoginResponse checkEmail(LoginData ld) {
		// TODO Auto-generated method stub
		return null;
	}


}
