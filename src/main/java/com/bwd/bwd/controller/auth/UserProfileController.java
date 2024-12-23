package com.bwd.bwd.controller.auth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import com.bwd.bwd.model.auth.UserEmailsAuth;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.repository.UserAddAuthRepo;
import com.bwd.bwd.repository.UserEmailAuthRepo;
import com.bwd.bwd.request.AddressData;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.AddressResponse;
import com.bwd.bwd.response.EmailResponse;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.cityInfo;
import com.bwd.bwd.response.countryInfo;
import com.bwd.bwd.serviceimpl.JwtUserToken;

@CrossOrigin("*")
@RequestMapping(path = "user/profile", produces = "application/json")
@RestController
public class UserProfileController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserAuthController auc;		

	@Autowired
	UserEmailAuthRepo uer;

	@Autowired
	UserAddAuthRepo uar;

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

	@PostMapping("/updateemailarchive")
	public ResponseEntity<StatusResponse> updateEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {

					String updateQuery = "UPDATE user_email_tbl SET archived = 1 WHERE bwd_email_id = ? ";
					jdbcTemplate.update(updateQuery, data.getEmail_id());

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  														
					entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");    
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}


	@PostMapping("/updateemailtype")
	public ResponseEntity<StatusResponse> updateEmailType(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {

					String updateQuery = "UPDATE user_email_tbl SET type_id = ? WHERE bwd_email_id = ? ";
					jdbcTemplate.update(updateQuery, data.getType_id(), data.getEmail_id());

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  														
					entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");    
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
	@PostMapping("/emailadd")
	public ResponseEntity<StatusResponse> emailadd(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {
					String sql1 =  "SELECT useraccountid FROM user_accounts WHERE userid = ?";
					int UID = jdbcTemplate.queryForObject(sql1, Integer.class, data.getUserid());

					String count = "SELECT count(bwd_email_id) FROM user_email_tbl WHERE email=? and archived =0";				
					int emailCount = 0; 
					try {
						emailCount = jdbcTemplate.queryForObject(count, Integer.class, data.getEmail());
					} catch (EmptyResultDataAccessException e) {	   
						emailCount = 0; 
					}  		

					String sql2 = "SELECT type_id FROM type_tbl WHERE type = LOWER(?)";
					int type_id = jdbcTemplate.queryForObject(sql2, Integer.class, data.getType());

					if(emailCount == 0)
					{				
						UserEmailsAuth uea = new UserEmailsAuth();
						int primary = 0;
						uer.save(uea.createEmail1(UID, data.getEmail(),type_id, primary));		

						sr.setValid(true);    
						sr.setStatusCode(1);
						sr.setMessage("Authenticate User Success");  
						entity = new ResponseEntity<>(sr, headers, HttpStatus.OK); 
					}
					else {
						sr.setValid(false);    
						sr.setStatusCode(2);
						sr.setMessage("Email already exist");  
						entity = new ResponseEntity<>(sr, headers, HttpStatus.BAD_REQUEST); 
					}
				}catch(NullPointerException npex) {

					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   		        
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);    
				}
				catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");			

					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}	
			} else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			

			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}

	@PostMapping("/getcountry")
	public ResponseEntity<AddressResponse> getcountry(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {
		ResponseEntity<AddressResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		AddressResponse rr = new AddressResponse();
		StatusResponse sr = new StatusResponse();
		List<countryInfo> ciList = new ArrayList<>();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {

					String sql = "select country, country_code from country_tbl";
					List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

					for (Map<String, Object> row : rows) {
						String email = (String) row.get("country");
						String country_code = (String) row.get("country_code");

						countryInfo ci = new countryInfo();
						ci.setCountry(email);
						ci.setCountryCode(country_code);
						ciList.add(ci);
					}

					sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage("Country fetched successfully");

					rr.setStatus(sr);
					rr.setCountries(ciList);

					entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);
				} catch (NullPointerException npex) {
					npex.printStackTrace();

					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");

					rr.setStatus(sr);
					entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();

					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");

					rr.setStatus(sr);
					entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				rr.setStatus(sr);
				entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			rr.setStatus(sr);
			entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
		}

		return entity;
	}



	@PostMapping("/getcity")
	public ResponseEntity<AddressResponse> getcity(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {
		ResponseEntity<AddressResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		AddressResponse rr = new AddressResponse();
		StatusResponse sr = new StatusResponse();
		List<cityInfo> ciList = new ArrayList<>();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {

					String sql = "select place_name, admin_name1, admin_name2 from world_postal_codes where postal_code = ? and country_code = ?";
					List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, data.getPostal_code(), data.getCountry_code());	            

					if (rows.isEmpty()) {

						sr.setValid(false); 
						sr.setStatusCode(0);
						sr.setMessage("No records found");
					} else {

						int index = 1; 

						for (Map<String, Object> row : rows) {

							String place_name = (String) row.get("place_name");
							String admin_name1 = (String) row.get("admin_name1");
							String admin_name2 = (String) row.get("admin_name2");

							cityInfo ci = new cityInfo();
							ci.setCity(place_name);
							ci.setAdmin_name1(admin_name1);
							ci.setAdmin_name2(admin_name2);

							ci.setIndex(index); 

							ciList.add(ci);
							index++;
						}

						sr.setValid(true); 
						sr.setStatusCode(1);
						sr.setMessage("Cities fetched successfully");

						rr.setCities(ciList);
					}
					rr.setStatus(sr);	               	               
					entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);

				} catch (NullPointerException npex) {
					npex.printStackTrace();

					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");

					rr.setStatus(sr);
					entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();

					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");

					rr.setStatus(sr);
					entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				rr.setStatus(sr);
				entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			rr.setStatus(sr);
			entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
		}

		return entity;
	}

	@PostMapping("/saveaddress")
	public ResponseEntity<StatusResponse> saveaddress(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {
					String sql1 = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
					int UID = jdbcTemplate.queryForObject(sql1, Integer.class, data.getUserid());

					String checkSql = "SELECT COUNT(*) FROM user_details WHERE useraccountid = ?";
					int count = jdbcTemplate.queryForObject(checkSql, Integer.class, UID);

					if (count > 0) {

						String updateSql = "UPDATE user_details SET useraddress1 = ?, useraddress2 = ?, usercity = ?, userstate = ?, userzip = ?, usercountry = ?, usermailingsame = ?, usermailing1 = ?, usermailing2 = ?, usermailingcity = ?, usermailingstate = ?, usermailingzip = ?, usermailingcountry = ? WHERE useraccountid = ?";
						jdbcTemplate.update(updateSql, data.getUseraddress1(), data.getUseraddress2(), data.getUsercity(), data.getUserstate(), data.getUserzip(), data.getUsercountry(),
								data.getUsermailingsame(), data.getUsermailing1(), data.getUsermailing2(), data.getUsermailingcity(), data.getUsermailingstate(), data.getUsermailingzip(), data.getUsermailingcountry(), UID);
					} else {

						AddressData uea = new AddressData();
						uar.save(uea.createAdd(UID, data.getUseraddress1(), data.getUseraddress2(), data.getUsercity(), data.getUserstate(), data.getUserzip(), data.getUsercountry(),
								data.getUsermailingsame(), data.getUsermailing1(), data.getUsermailing2(), data.getUsermailingcity(), data.getUsermailingstate(), data.getUsermailingzip(), data.getUsermailingcountry()));
					}

					sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");
					entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);

				} catch (NullPointerException npex) {
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}
				catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");			

					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}	
				
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}

	@PostMapping("/updatenumber")
	public ResponseEntity<StatusResponse> updatenumber(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<StatusResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {


					String sql1 = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
					int UID = jdbcTemplate.queryForObject(sql1, Integer.class, data.getUserid());

					String count = "SELECT count(tel) FROM user_tel_tbl WHERE tel=? and archived =0";				
					int emailCount = 0; 
					try {
						emailCount = jdbcTemplate.queryForObject(count, Integer.class, data.getTel());
					} catch (EmptyResultDataAccessException e) {	   
						emailCount = 0; 
					}  

					if(emailCount > 0)
					{
						sr.setValid(false);
						sr.setStatusCode(2);
						sr.setMessage("Record already exist");
						entity = new ResponseEntity<>(sr, headers, HttpStatus.BAD_REQUEST);

					}
					else {

						String checkSql = "SELECT COUNT(*) FROM user_tel_tbl WHERE useraccountid = ?";
						int rowcount = jdbcTemplate.queryForObject(checkSql, Integer.class, UID);

						if (rowcount > 0) {

							String updateSql = "UPDATE user_tel_tbl SET tel = ?, tel_code = ? WHERE useraccountid = ? and archived =0";	
							jdbcTemplate.update(updateSql, data.getTel(), data.getTelCode(), UID);


						}else {

							Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

							String insert = "INSERT into user_tel_tbl(useraccountid, tel, tel_code, date_verified, date_added) VALUES (?, ?, ?, ?, ? ) ";
							jdbcTemplate.update(insert, UID,  data.getTel(), data.getTelCode(), currentTimestamp, currentTimestamp);
						}

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Authenticate User Success");
						entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);

					}
				} catch (NullPointerException npex) {
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}
				catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");			

					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}	
				
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
		}
		
		 System.out.println("ResponseEntity: " + entity);
		return entity;
	}


	@PostMapping("/primaryemail")
	public ResponseEntity<StatusResponse> primaryemail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<StatusResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {
					String sql1 = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
					int UID = jdbcTemplate.queryForObject(sql1, Integer.class, data.getUserid());
					
					String pri = "SELECT bwd_email_id FROM user_email_tbl WHERE useraccountid=? and `primary` =1";				
					int primary = jdbcTemplate.queryForObject(pri, Integer.class, UID);
					
					System.out.println(primary);

					String updateSql = "UPDATE user_email_tbl SET `primary` = 0 WHERE bwd_email_id = ?";	
					jdbcTemplate.update(updateSql,primary) ;

					String updateSql1 = "UPDATE user_email_tbl SET `primary` = 1 WHERE bwd_email_id = ?";	
					jdbcTemplate.update(updateSql1, data.getEmail_id());
		                
		                sr.setValid(true);
		                sr.setStatusCode(1);
		                sr.setMessage("Authenticate User Success");
		                entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);
		                
					
	            } catch (NullPointerException npex) {
	                sr.setValid(false);
	                sr.setStatusCode(0);
	                sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
	                entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
	            }
				catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");			

					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}	
	        } else {
	            sr.setValid(false);
	            sr.setStatusCode(21);
	            sr.setMessage("Unauthentic Access Token");
	            entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
	        }
	    } else {
	        sr.setValid(false);
	        sr.setStatusCode(20);
	        sr.setMessage("Unauthentic Token");
	        entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
	    }
		
		 System.out.println("ResponseEntity: " + entity);
		 
	    return entity;
	}
}