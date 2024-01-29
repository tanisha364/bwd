package com.bwd.bwd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.model.UserAccounts;
import com.bwd.bwd.model.UserEmails;
import com.bwd.bwd.repository.UserAccountsRepo;
import com.bwd.bwd.repository.UserEmailsRepo;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.response.LoginResponse;
import com.bwd.bwd.service.LoginServices;
import com.bwd.bwd.serviceimpl.LoginServicesImpl;

@CrossOrigin("*")
@RestController
@RequestMapping(path="/login",  produces="application/json")
public class LoginController {
	
	@Autowired
	UserEmailsRepo uer;
	
	@Autowired
	UserAccountsRepo uar;

	
	@GetMapping()
	public String welcome()
	{
		return "Welcome to World of BWD"; 
	}
	
	@GetMapping("/showall")
	public List<UserEmails> showall()
	{
		return uer.findAll();		
	}
	
	@PostMapping("/checkemail")
	public LoginResponse checkUserEmail(@RequestBody LoginData data)
	{
		LoginResponse lr = new LoginResponse();
		UserAccounts ua = new UserAccounts();
		
		
		UserEmails ue = new UserEmails();
		
		ue = uer.findByEmail(data.getEmail());
		
		lr.setEmail(ue.getEmail());
		lr.setUseraccount(ue.getUseraccountid());	
				
		int userlevel = ua.getUserlevel();
		lr.setUserStatus(userlevel);
		lr.setRegnum(ua.getRegnum());
		
		if(userlevel == -10)
		{
			lr.setMessage("Email verification is not completed");
			lr.setValid(false);						
		}
		else
		{
			lr.setMessage("Welcome go ahead");
			lr.setValid(true);
		}
		
		return lr;
	}
		

	@PostMapping("/validate")
	public ResponseEntity<LoginResponse> checkUser(@RequestBody LoginData data)
	{		
		LoginServices ls = new LoginServicesImpl();
		LoginResponse lr = new LoginResponse();
		LoginResponse lrt = new LoginResponse();
		UserAccounts ua = new UserAccounts();		
		UserEmails ue = new UserEmails();
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<LoginResponse> entity = null;
		
		try
		{		
			ue = uer.findByEmail(data.getEmail());	
			
			lr.setEmail(ue.getEmail());
			lr.setUseraccount(ue.getUseraccountid());	
			lr.setUserStatus(2);
			
			int userlevel = ua.getUserlevel();
			lr.setUserStatus(3);
			lr.setUserLevel(userlevel);
			lr.setRegnum(ua.getRegnum());
			
			if(userlevel == -10)
			{
				lr.setMessage("Email verification is not completed");
				lr.setValid(false);			
				lr.setUserStatus(4);    
			}
			else
			{
				lr.setUserStatus(5); 				
				lrt = ls.checkPassword(data, ua);
			
				lr.setUserStatus(lrt.getUserStatus());
				lr.setPassword(lrt.getPassword());
				
				if(lrt.getUserStatus() == 2 )
				{
					lr.setMessage("Welcome go ahead");
					lr.setUserStatus(1);    
					lr.setValid(true);
				}
				else {
					lr.setMessage("Incorrect UserId or Password !!");
					lr.setUserStatus(6);    
					lr.setValid(false);					
				}
			}
			entity = new ResponseEntity<>(lr,headers,HttpStatus.OK);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			lr.setUserStatus(9);   // 4 - UserStatus  when UserLevel = -10 
			lr.setMessage("Exception Occurred"); //ex.getMessage());
			entity = new ResponseEntity<>(lr,headers,HttpStatus.UNAUTHORIZED);			
		}
		return entity;
//		return lr;
	}	
	
	
	/*
	 *  UserStatus 
	 *  If All Ok and Login to Welcome                      UserStatus will be 1
	 *  If Email Ok and User Account can Check              UserStatus will be 2
	 *  If UserAccount found based on useraccountid         UserStatus will be ------3     
	 *  If User Level found Further Check                   UserStatus will be 3
	 *  If User Level found -10 Further Che                 UserStatus will be 4
	 *  If User Level found  1 Further Move                 UserStatus will be 5
	 *  If User Password is Not Ok No Further Move          UserStatus will be 6  
	 *  If Exception Occurred Not Ok No Further Move        UserStatus will be 9  
	 */
}