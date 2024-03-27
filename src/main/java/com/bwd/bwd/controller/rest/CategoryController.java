package com.bwd.bwd.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.controller.auth.UserAuthController;
import com.bwd.bwd.response.CategoryInfoResponse;
import com.bwd.bwd.response.CategoryResponse;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.UserDataResponse;
import com.bwd.bwd.response.UserInfo;
import com.bwd.bwd.response.UserInfoResponse;
import com.bwd.bwd.model.Category;
import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.repository.CategoryRepo;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.request.UserData;

@CrossOrigin("*")
@RequestMapping(path = "/rest/category", produces = "application/json")
@RestController

public class CategoryController 
{
	@Autowired
	UserAuthController auc;	
	
	@Autowired 
	UserAuthController uac;	
	
	@Autowired
	UserAccountsAuthRepo uaar;		
	
	@Autowired
	CategoryRepo cr;
	
	
	@GetMapping("/jobsmith")
    public ResponseEntity<CategoryInfoResponse> getCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader)
    {
		ResponseEntity<CategoryInfoResponse> entity;
//		ResponseEntity<CategoryResponse> entity;
    	HttpHeaders headers = new HttpHeaders();
   	
		ResponseEntity<TokenResponse> entityToken = null;    	
    	CategoryInfoResponse catinforesp = new CategoryInfoResponse();    	
    	CategoryResponse catresp = new CategoryResponse();   
    	TokenResponse tr = new TokenResponse();
    	StatusResponse sr = new StatusResponse();
    	
    	boolean validToken = false;
    	
    	entityToken = uac.validateBearerToken(authorizationHeader);
		
		tr = entityToken.getBody();		
		StatusResponse srToken = tr.getStatus();		
		validToken = srToken.isValid();
		
		
    	List<Category> list = null;

    	if(validToken)
		{	
    		sr.setValid(true);
			sr.setStatusCode(1);
			sr.setMessage("Category List with Authentic Token");
    		list = cr.findByCategoryType();
		}
    	else
    	{
    		sr.setValid(false);
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
    	}
    	
    	catresp.setCategories(list);
    	
    	catinforesp.setData(catresp);
    	catinforesp.setStatus(sr);
    	
    	entity = new ResponseEntity<>(catinforesp, headers, HttpStatus.OK);
    	
    	return entity;    	
    	
    }  
	
    public boolean checkToken(String authorizationHeader)
    {
    	boolean validToken = false;
    	
    	TokenResponse tr = new TokenResponse();
    	ResponseEntity<TokenResponse> entityToken = null;
    	
    	entityToken = auc.validateBearerToken(authorizationHeader);
		
    	tr = entityToken.getBody();		
		StatusResponse srToken = tr.getStatus();		
		validToken = srToken.isValid();
    	
    	return validToken;
    }	
}
