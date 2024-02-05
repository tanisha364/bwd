package com.bwd.bwd.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.controller.auth.UserAuthController;
import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.UserDataResponse;
import com.bwd.bwd.response.UserInfo;
import com.bwd.bwd.response.UserInfoResponse;

@CrossOrigin("*")
@RestController
@RequestMapping(path="/rest/user",  produces="application/json")
public class UserController {

	@Autowired 
	UserAuthController uac;	
	
	@Autowired
	UserAccountsAuthRepo uaar;
	
	@GetMapping()
	public String welcome()
	{
		return "Welcome to World of BWD Dashboard User calls"; 
	}
	
	@PostMapping("/userinfo")
	public ResponseEntity<UserInfoResponse> validateUser(@RequestBody UserData data)
	{	
		ResponseEntity<UserInfoResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		UserInfoResponse uir = new UserInfoResponse();
		UserAccountsAuth uaa = new UserAccountsAuth();
		StatusResponse sr = new StatusResponse();
		UserDataResponse udr = new UserDataResponse();
		UserInfo ui = new UserInfo();		
				
		try
		{	
			System.out.println(data.getUseraccountid());
			uaa = uaar.getReferenceById(data.getUseraccountid());
		
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

			entity = new ResponseEntity<>(uir,headers,HttpStatus.OK);	
		}
		catch(Exception ex)
		{
			UserInfo ui_null = new UserInfo();
			System.out.println(ex.getMessage());
//			ui.setFirstname(null);
//			ui.setLastname(null);
//			ui.setStatus(0);
//			ui.setStatusdate(null);			
			
			sr.setValid(false);
			sr.setStatusCode(9);
			sr.setMessage("Exception Occurred : Useracoountid provided does not match in our record");			

			udr.setUserinfo(ui_null);
			uir.setStatus(sr);			
			uir.setData(udr);

			entity = new ResponseEntity<>(uir,headers,HttpStatus.NOT_FOUND);			
		}
		
		return entity;
	}
	
	@PostMapping("/userdetails")
	public ResponseEntity<UserInfoResponse> validateUserUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data)
	{	
		ResponseEntity<UserInfoResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		UserInfoResponse uir = new UserInfoResponse();
		UserAccountsAuth uaa = new UserAccountsAuth();
		StatusResponse sr = new StatusResponse();
		UserDataResponse udr = new UserDataResponse();
		UserInfo ui = new UserInfo();
		UserInfo ui_null = new UserInfo();

		ResponseEntity<TokenResponse> entityToken = null;
		TokenResponse tr = new TokenResponse();
    	    	
    	boolean validToken = false;
    	
    	entityToken = uac.validateBearerToken(authorizationHeader);
		
		tr = entityToken.getBody();		
		StatusResponse srToken = tr.getStatus();		
		validToken = srToken.isValid();	
		System.out.println(srToken.getMessage());
		sr = srToken;

		if (validToken) {
			try {
				System.out.println(data.getUseraccountid());
				uaa = uaar.getReferenceByUserid(data.getUserid());

	//			ui.setUseraccountid(uaa.getUseraccountid());
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

				entity = new ResponseEntity<>(uir, headers, HttpStatus.OK);
			} catch (Exception ex) {				
				System.out.println(ex.getMessage());

				sr.setValid(false);
				sr.setStatusCode(9);
				sr.setMessage("Exception Occurred : Useracoountid provided does not match in our record");

				udr.setUserinfo(ui_null);
				uir.setStatus(sr);
				uir.setData(udr);

				entity = new ResponseEntity<>(uir, headers, HttpStatus.NOT_FOUND);
			}
		} else {
			System.out.println(data.getUseraccountid());
			udr.setUserinfo(ui_null);
			uir.setStatus(sr);
			uir.setData(udr);
			entity = new ResponseEntity<>(uir, headers, HttpStatus.NOT_FOUND);
		}
		
		return entity;
	}	
	
	@PostMapping("/fetchuseraccountid")
	public long fetchUserAccountId(@RequestBody UserData data) {
		long useraccountid = -1l;
		UserAccountsAuth uaa = new UserAccountsAuth();
		
		try {	
			uaa = uaar.getReferenceByUserid(data.getUserid());
			useraccountid = uaa.getUseraccountid();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		return useraccountid;
	}
}