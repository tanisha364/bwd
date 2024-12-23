package com.bwd.bwd.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.controller.auth.UserAuthController;
import com.bwd.bwd.response.CategoryInfoResponse;
import com.bwd.bwd.response.CategoryResponse;
import com.bwd.bwd.response.Job;
import com.bwd.bwd.response.JobResponse;
import com.bwd.bwd.response.Resource;
import com.bwd.bwd.response.ResourceResponse;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.UserDataResponse;
import com.bwd.bwd.response.UserInfo;
import com.bwd.bwd.response.UserInfoResponse;
import com.bwd.bwd.serviceimpl.JwtUserToken;
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
	private JdbcTemplate jdbcTemplate;
	
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

	@PostMapping("/resource")
	public ResponseEntity<ResourceResponse> resource(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<ResourceResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		StatusResponse sr = new StatusResponse();
		ResourceResponse ir = new ResourceResponse();

		List<Resource> re = new ArrayList<>();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = uac.isValidAccessToken(data.getUserid());
		
		 String query = null;
		    Object[] params = null;

		if (validToken) {
			if (validAccessToken) {
				try {
					 if (data.getCompanyid() == 0) {
					        if (!"0".equals(data.getPageid())) {
			
					            query = "SELECT resource_name, resource_location " +
					                    "FROM resource_tbl " +
					                    "WHERE resource_companyid = 0 AND (resource_display_pg = ? OR resource_display_pg = '0') AND archived = 0";
					            params = new Object[]{data.getPageid()};
					        } else {
					          
					            query = "SELECT resource_name, resource_location " +
					                    "FROM resource_tbl " +
					                    "WHERE resource_companyid = 0 AND resource_display_pg = '0' AND archived = 0";
					            params = new Object[]{};
					        }
					    } else if (!"0".equals(data.getPageid())) {
					        
					        query = "SELECT resource_name, resource_location " +
					                "FROM resource_tbl " +
					                "WHERE (resource_companyid = 0 AND (resource_display_pg = ? OR resource_display_pg = '0')) " +
					                "OR (resource_companyid = ? AND resource_display_pg = ?) AND archived = 0";
					        params = new Object[]{data.getPageid(), data.getCompanyid(), data.getPageid()};
					    }else {
					    	
					    	sr.setValid(false);    
							sr.setStatusCode(2);
							sr.setMessage("No record found");  	

							ir.setStatus(sr);
					    	  	entity = new ResponseEntity<>(ir, headers, HttpStatus.BAD_REQUEST);
					    	  	return entity;
					    }
					 
					  List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params);
					    for (Map<String, Object> row : rows) {
					        Resource resource = new Resource();
					        resource.setHelp_resource_name((String) row.get("resource_name"));
					        resource.setHelp_resource_location((String) row.get("resource_location"));
					        re.add(resource);
					    }

					ir.setData(re);	

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  	

					ir.setStatus(sr);
					entity = new ResponseEntity<>(ir, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				ir.setStatus(sr);
				entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");   
			ir.setStatus(sr);
			entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
	
	
	@PostMapping("/help_resource")
	public ResponseEntity<ResourceResponse> helpResource(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<ResourceResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		StatusResponse sr = new StatusResponse();
		ResourceResponse ir = new ResourceResponse();

		List<Resource> re = new ArrayList<>();

		boolean validToken = checkToken(authorizationHeader);

		
		boolean validAccessToken = uac.isValidAccessToken(data.getUserid());
		
		 String query = null;
		    Object[] params = null;

		if (validToken) {
			if (validAccessToken) {
				try {
					 if (data.getCompanyid() == 0) {
					        if (!"0".equals(data.getPageid())) {
			
					            query = "SELECT help_resource_name, help_resource_location " +
					                    "FROM help_resource_tbl " +
					                    "WHERE help_resource_companyid = 0 AND (help_resource_display_pg = ? OR help_resource_display_pg = '0') AND archived = 0";
					            params = new Object[]{data.getPageid()};
					        } else {
					          
					            query = "SELECT help_resource_name, help_resource_location " +
					                    "FROM help_resource_tbl " +
					                    "WHERE help_resource_companyid = 0 AND help_resource_display_pg = '0' AND archived = 0";
					            params = new Object[]{};
					        }
					    } else if (!"0".equals(data.getPageid())) {
					        
					        query = "SELECT help_resource_name, help_resource_location " +
					                "FROM help_resource_tbl " +
					                "WHERE (help_resource_companyid = 0 AND (help_resource_display_pg = ? OR help_resource_display_pg = '0')) AND archived = 0 " +
					                "OR (help_resource_companyid = ? AND help_resource_display_pg = ?)";
					        params = new Object[]{data.getPageid(), data.getCompanyid(), data.getPageid()};
					    }else {
					    	
					    	sr.setValid(false);    
							sr.setStatusCode(2);
							sr.setMessage("No record found");  	

							ir.setStatus(sr);
					    	  	entity = new ResponseEntity<>(ir, headers, HttpStatus.BAD_REQUEST);
					    	  	return entity;
					    }
					 
					  List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params);
					    for (Map<String, Object> row : rows) {
					        Resource resource = new Resource();
					        resource.setHelp_resource_name((String) row.get("help_resource_name"));
					        resource.setHelp_resource_location((String) row.get("help_resource_location"));
					        re.add(resource);
					    }

					ir.setData(re);	

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  	

					ir.setStatus(sr);
					entity = new ResponseEntity<>(ir, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				}
			}else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				ir.setStatus(sr);
				entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");    
			ir.setStatus(sr);
			entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
		}
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
