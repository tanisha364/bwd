package com.bwd.bwd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
}