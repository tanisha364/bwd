package com.bwd.bwd.controller.rest;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.bwd.bwd.controller.auth.UserAuthController;
import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.request.KeyInput;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.UserDataResponse;
import com.bwd.bwd.response.UserInfo;
import com.bwd.bwd.response.UserInfoResponse;
import com.bwd.bwd.response.addressInfo;
import com.bwd.bwd.response.billaddrInfo;
import com.bwd.bwd.response.emailInfo;
import com.bwd.bwd.response.mailaddrInfo;
import com.bwd.bwd.serviceimpl.JwtUserToken;
import com.bwd.bwd.serviceimpl.UserInfoImpl;

@CrossOrigin("*")
@RestController
@RequestMapping(path="/rest/user",  produces="application/json")
public class UserController {

	@Autowired 
	UserAuthController uac;	
	
	@Autowired
	UserAccountsAuthRepo uaar;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	@SuppressWarnings("deprecation")
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
		
		 List<emailInfo> ei = new ArrayList<>();
		 

		ResponseEntity<TokenResponse> entityToken = null;
		TokenResponse tr = new TokenResponse();
    	    	
    	boolean validToken = false;
    	
    	entityToken = uac.validateBearerToken(authorizationHeader);
		
		tr = entityToken.getBody();		
		StatusResponse srToken = tr.getStatus();		
		validToken = srToken.isValid();	

		boolean validAccessToken = uac.isValidAccessToken(data.getUserid());
		
		if(validToken)
		{
			if(validAccessToken)
			{		
			try {
				System.out.println(data.getUseraccountid());
				uaa = uaar.getReferenceByUserid(data.getUserid());
				
				int uid = uaa.getUseraccountid();
				BigDecimal status = uaa.getStatus();
				
				String sql3 = "SELECT IFNULL((SELECT tel FROM user_tel_tbl WHERE useraccountid = ? and archived = 0 LIMIT 1), '0') AS tel";
				String tel = jdbcTemplate.queryForObject(sql3, String.class, uid);

				if (tel == null || tel.equals("0")) {
                	tel = "";
                }

				String sql1 = "SELECT statusdesc, progress_bar, css_color, status_help from status_tbl st where statusid = "+status;				
				jdbcTemplate.query(sql1, new Object[] {}, rs -> {
					ui.setStatusDesc(rs.getString("statusdesc"));
					ui.setProgress_bar(rs.getInt("progress_bar"));
					ui.setCss_color(rs.getString("css_color"));
					ui.setStatus_help(rs.getString("status_help"));
				});
				
				
				String query = "select bwd_email_id, email, type_id,`primary` from user_email_tbl where archived = 0 and useraccountid ="+uid;
			
				List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

				for (Map<String, Object> row : rows) {
					int mail_id = (int) row.get("bwd_email_id");
					String email = (String) row.get("email");
					int type_id = (int) row.get("type_id");
					int primary = (int) row.get("primary");
					
					String sql2 = "SELECT type FROM type_tbl WHERE type_id = ?";
					String type = jdbcTemplate.queryForObject(sql2, String.class, type_id);

					emailInfo eiObj = new emailInfo();
					eiObj.setBwd_email_id(mail_id);
                    eiObj.setEmail(email);
                    eiObj.setType(type);
                    eiObj.setPrimary(primary);
                    eiObj.setType_id(type_id);
                    ei.add(eiObj);
				}
				
				
				String addr = "select * from user_details where useraccountid ="+uid;
            	List<Map<String, Object>> aadrrow = jdbcTemplate.queryForList(addr);

            	addressInfo add = new addressInfo();
            	            	
				for (Map<String, Object> row : aadrrow) {
					
					billaddrInfo billaddr = new billaddrInfo();
	            	mailaddrInfo mailaddr = new mailaddrInfo();
					
					String useraddress1 = (String) row.get("useraddress1");
				    String useraddress2 = (String) row.get("useraddress2");
				    String usercity = (String) row.get("usercity");
				    String userstate = (String) row.get("userstate");
				    String userzip = (String) row.get("userzip");
				    String usercountry = (String) row.get("usercountry");
				    
				    billaddr.setUseraddress1(useraddress1);
				    billaddr.setUseraddress2(useraddress2);
				    billaddr.setUsercity(usercity);
				    billaddr.setUserstate(userstate);
				    billaddr.setUserzip(userzip);
				    billaddr.setUsercountry(usercountry);
				    
				    
				    String usermailing1 = (String) row.get("usermailing1");
				    String usermailing2 = (String) row.get("usermailing2");
				    String usermailingcity = (String) row.get("usermailingcity");
				    String usermailingstate = (String) row.get("usermailingstate");
				    String usermailingzip = (String) row.get("usermailingzip");
				    String usermailingcountry = (String) row.get("usermailingcountry");

				    mailaddr.setUsermailing1(usermailing1);
				    mailaddr.setUsermailing2(usermailing2);
				    mailaddr.setUsermailingcity(usermailingcity);
				    mailaddr.setUsermailingstate(usermailingstate);
				    mailaddr.setUsermailingzip(usermailingzip);	
				    mailaddr.setUsermailingcountry(usermailingcountry);
				    
				    int usermailingsame = (int) row.get("usermailingsame");
				    
				    add.setBilling(billaddr);
				    add.setMailing(mailaddr);
				    add.setIsusermailingsame(usermailingsame);
				}
				

				ui.setFirstname(uaa.getFirstname());
				ui.setLastname(uaa.getLastname());
				ui.setStatus(uaa.getStatus());
				ui.setStatusdate(uaa.getStatusdate());
				ui.setTel(tel);
				ui.setEmails(ei);
				ui.setAddress(add);
				
				
				sr.setValid(true);
				sr.setStatusCode(1);
				sr.setMessage("Welcome !!");

				udr.setUserinfo(ui);
				uir.setStatus(sr);
				uir.setData(udr);

				entity = new ResponseEntity<>(uir, headers, HttpStatus.OK);
			} catch (Exception ex) {				
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");

				udr.setUserinfo(ui_null);
				uir.setStatus(sr);
				uir.setData(udr);

				entity = new ResponseEntity<>(uir, headers, HttpStatus.UNAUTHORIZED);
			}
		}
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				uir.setData(null);
				uir.setStatus(sr);		
				entity = new ResponseEntity<>(uir, headers, HttpStatus.UNAUTHORIZED);
			}
		}else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");		
			uir.setStatus(sr);
			entity = new ResponseEntity<>(uir, headers, HttpStatus.UNAUTHORIZED);
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
//	
//	@GetMapping("/generatelinkid")
//	public String generateUniqueLinkId() {
//		String linkid = "-1"; 
//		
//		linkid = UserInfoImpl.generateUniqueLinkId();
//		
//		return linkid;
//	}
	
	@PostMapping("/generatelinkid")
	public String generateUniqueLinkId(@RequestBody KeyInput data) {
		String linkid = "-1"; 
		System.out.println("keylenght = "+data.getKeylenght());
		linkid = UserInfoImpl.generateUniqueLinkId(data.getKeylenght());
		
		return linkid;
	}	
}