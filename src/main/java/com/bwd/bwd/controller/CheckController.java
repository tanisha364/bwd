package com.bwd.bwd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.model.UserAccounts;
import com.bwd.bwd.repository.UserAccountsRepo;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.response.LoginResponse;
import com.bwd.bwd.service.LoginServices;
import com.bwd.bwd.serviceimpl.LoginServicesImpl;

@CrossOrigin("*")
@RestController
@RequestMapping(path="/check", produces="application/json")
public class CheckController {
	
	@Autowired
	UserAccountsRepo uar;
	
	@GetMapping()
	public String sayHello()
	{
		System.out.println("Hello World");
		return "Hello Dear";
	}
	
	@PostMapping("/hello")
	public String sayHello(@RequestBody LoginData ld)
	{
		return "Hello Dear : "+ ld.getEmail()+"  ---  "+ld.getPassword();		
	}
	
	@PostMapping("/validate")
	public ResponseEntity<LoginResponse> checkUser(@RequestBody LoginData ld)
	{	
		HttpHeaders headers = new HttpHeaders();	
		
		LoginServices ls = new LoginServicesImpl();
		
		LoginResponse lr = ls.checkUser(ld);
		
		ResponseEntity<LoginResponse> entity = new ResponseEntity<>(lr,headers,HttpStatus.OK);
		return entity;		
	}	


	@PostMapping("/emailcheck")
	public List<UserAccounts> checkEmail(@RequestBody LoginData ld)
	{			
		return uar.findByEmail(ld.getEmail());	
	}	
	
	@PostMapping("/saveme")
	public void saveme(@RequestBody UserAccounts ua)
	{
		uar.save(ua);
	}
	
	@GetMapping("/showall")
	public List<UserAccounts> showall()
	{
		return uar.findAll();
	}
}
