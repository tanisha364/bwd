package com.bwd.bwd.controller.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.bwd.bwd.response.UserInfo;
import com.bwd.bwd.response.company.CompanyDetailsResponse;
import com.bwd.bwd.response.company.CompanyListResponse;
import com.bwd.bwd.response.company.CompanyResponse;
import com.bwd.bwd.response.company.DetailsCompanyResponse;
import com.bwd.bwd.response.company.ResponseCompany;
import com.bwd.bwd.response.company.ResponseDetailCompany;
import com.bwd.bwd.service.company.CompanyServices;
import com.bwd.bwd.serviceimpl.JwtUserToken;

@CrossOrigin("*")
@RequestMapping(path = "/rest/company", produces = "application/json")
@RestController
public class CompanyController {

	@Autowired
	private CompanyServices cs;
	
	@Autowired
	UserAuthController auc;	
	
	@Autowired
	UserAccountsAuthRepo uaar;
	
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
	
	@PostMapping("/companylist")
	public ResponseEntity<ResponseCompany> getCompanyList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData requestData)
	{		
		
		ResponseEntity<ResponseCompany> entity;
		HttpHeaders headers = new HttpHeaders();
		
		ResponseCompany rc = new ResponseCompany();
		StatusResponse sr = new StatusResponse();    	
		CompanyResponse cr = new CompanyResponse();
		
		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(requestData.getUserid());
		
		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					try
					{ 
						uaa = uaar.getReferenceByUserid(requestData.getUserid());   
						System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+requestData.getUserid());
						ui.setFirstname(uaa.getFirstname());
						ui.setLastname(uaa.getLastname());
						ui.setStatus(uaa.getStatus());
						ui.setStatusdate(uaa.getStatusdate());   

						List<CompanyListResponse> objects = cs.findCompanyList(requestData);

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Company List");      		    	

						cr.setCompanyListResponse(objects);

						rc.setStatus(sr);
						rc.setData(cr);

						entity = new ResponseEntity<>(rc, headers, HttpStatus.OK);	    				
					}catch(NullPointerException npex)
					{
						npex.printStackTrace();
						System.out.println(npex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
						rc.setData(null);
						rc.setStatus(sr);
						entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);    				

					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or Unauthentic User");
						rc.setData(null);
						rc.setStatus(sr);
						entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
					}  		
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rc.setData(null);
					rc.setStatus(sr);
					entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
				}
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rc.setData(null);
				rc.setStatus(sr);		
				entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rc.setStatus(sr);
			entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
	
	@PostMapping("/companydetail")
	public ResponseEntity<ResponseDetailCompany> getCompanyDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData requestData)
	{	
		ResponseEntity<ResponseDetailCompany> entity;
		HttpHeaders headers = new HttpHeaders();
		
		ResponseDetailCompany rc = new ResponseDetailCompany();
		StatusResponse sr = new StatusResponse();    	
		DetailsCompanyResponse cr = new DetailsCompanyResponse();
		
		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(requestData.getUserid());
		
		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					try
					{ 
						uaa = uaar.getReferenceByUserid(requestData.getUserid());   
						System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+requestData.getUserid());
						ui.setFirstname(uaa.getFirstname());
						ui.setLastname(uaa.getLastname());
						ui.setStatus(uaa.getStatus());
						ui.setStatusdate(uaa.getStatusdate());   

						List<CompanyDetailsResponse> objects = cs.findCompanyDetaist(requestData);

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Company Details");      		    	

						cr.setCompanyDetailsResponse(objects);
						rc.setStatus(sr);
						rc.setData(cr);

						entity = new ResponseEntity<>(rc, headers, HttpStatus.OK);	    				
					}catch(NullPointerException npex)
					{
						npex.printStackTrace();
						System.out.println(npex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
						rc.setData(null);
						rc.setStatus(sr);
						entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);    				

					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or Unauthentic User");
						rc.setData(null);
						rc.setStatus(sr);
						entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
					}  		
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rc.setData(null);
					rc.setStatus(sr);
					entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
				}
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rc.setData(null);
				rc.setStatus(sr);		
				entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rc.setStatus(sr);
			entity = new ResponseEntity<>(rc, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
}
