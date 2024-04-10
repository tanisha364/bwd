package com.bwd.bwd.controller.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.db.DBOperation;
import com.bwd.bwd.model.auth.AccountRequest;
import com.bwd.bwd.repository.OauthClientsRepo;
import com.bwd.bwd.response.RegPageResponse;
import com.bwd.bwd.response.RegTextResponse;
import com.bwd.bwd.response.RegisterResponse;
import com.bwd.bwd.response.SavedJobReport;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TextResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.service.company.RegisterPageService;

@CrossOrigin("*")
@RequestMapping(path = "/register", produces = "application/json")
@RestController
public class RegisterPageController {
	
String 	tokenType = "Bearer";
	
	@Autowired
	OauthClientsRepo ocr;	
	
	@Autowired
	private RegisterPageService rps;
	
	@Autowired
	UserAuthController auc;	
	
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
	
	@PostMapping("/page")
	public ResponseEntity<TextResponse> registerText(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody AccountRequest userAccount) {
		
		ResponseEntity<TextResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		TextResponse tr = new TextResponse();
		StatusResponse sr = new StatusResponse();
		RegPageResponse rr = new RegPageResponse();
		
		
		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if(validToken)
		{
			try {
				List<RegTextResponse> rtr = rps.getPageReport(userAccount);
				rr.setRt(rtr);
				sr.setValid(true);
				sr.setStatusCode(1);
				sr.setMessage("Registration page");                	

				tr.setStatus(sr);
				tr.setData(rr);
				entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);     
			}catch (Exception ex) {
				ex.printStackTrace();	
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				tr.setData(null);
				tr.setStatus(sr);
				entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
			}			
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);	
			return entity;
		} 
		return entity;
	}
	
	
}
