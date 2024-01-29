package com.bwd.bwd.serviceimpl;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.model.auth.UserEmailsAuth;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.repository.UserEmailAuthRepo;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.response.AuthInfo;
import com.bwd.bwd.response.AuthResponse;
import com.bwd.bwd.response.DataResponse;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.service.AuthServices;

public class AuthServiceImpl implements AuthServices {
	
	@Autowired
	UserEmailAuthRepo uer;	
	
	@Autowired
	ResponseEntity<AuthResponse> entity = null;
	
	@Autowired
	HttpHeaders headers;
	
	@Autowired
	UserAccountsAuthRepo uaar;
		
	@Autowired
	AuthResponse ar;

	@Override
	public AuthResponse authenticateUser(LoginData data) {
		AuthServices as = new AuthServiceImpl();		
		UserAccountsAuth uaa = new UserAccountsAuth();	 	
		UserEmailsAuth uea = new UserEmailsAuth();
		HttpHeaders headers = new HttpHeaders(); 		
		AuthResponse ar  = new AuthResponse();	
		AuthInfo ai = new AuthInfo();
		StatusResponse sr = new StatusResponse();	
		DataResponse dr = new DataResponse();
		
		try {
			uea = uer.findByEmail(data.getEmail());		 	
			ai.setEmail(uea.getEmail());
			ai.setUseraccountid(uea.getUseraccountid());
			sr.setStatusCode(2);		
			
			uaa = uaar.getReferenceById(uea.getUseraccountid()); 
			
			ar = as.checkUser(uaa,ar);		
			
			ar = as.checkLevel(ai.getUserLevel(), ar);	
			
			if(ai.getUserLevel() == 1 || ai.getUserLevel() == 8 || ai.getUserLevel() == 9 )
			{
				ar = as.checkPassword(data, uaa,ar);	
				if(sr.getStatusCode() == 2 )
				{
					sr.setValid(true);
					sr.setStatusCode(1);  
					sr.setMessage("Welcome go ahead !!");					
				}
				else {
					sr.setMessage("Incorrect UserId or Password !!");
					sr.setStatusCode(0);    
					sr.setValid(false);					
				}
			} 
		    dr.setUserinfo(ai);
		    ar.setData(dr);
		    ar.setStatus(sr);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			sr.setStatusCode(9);  
			sr.setMessage("Exception Occurred"); 
		    dr.setUserinfo(ai);
		    ar.setData(dr);
		    ar.setStatus(sr);
			entity = new ResponseEntity<>(ar,headers,HttpStatus.UNAUTHORIZED);				
		}
		return ar;
	}	
	
	@Override
	public AuthResponse checkUser(UserAccountsAuth uaa,AuthResponse ar)
	{
		long useraccountid = -1;
		AuthInfo ai = new AuthInfo();
		StatusResponse sr = new StatusResponse();
		DataResponse dr = new DataResponse();
		
		try
		{				
			useraccountid = uaa.getUseraccountid();
			int userlevel = uaa.getUserlevel();	
			
			ai.setUseraccountid(useraccountid);
			ai.setUserLevel(userlevel);
			ai.setEmail(uaa.getEmail());
			
			sr.setStatusCode(3);  
			sr.setMessage("User Account Found for Given Useraccount Id"); 
			
		    dr.setUserinfo(ai);
		    ar.setData(dr);
		    ar.setStatus(sr);
		    
			entity = new ResponseEntity<>(ar,headers,HttpStatus.FOUND);			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage() +useraccountid);
			sr.setStatusCode(9);  
			sr.setMessage("Exception Occurred - User Not Found for Given Useraccount Id");
		    ar.setStatus(sr);
			entity = new ResponseEntity<>(ar,headers,HttpStatus.UNAUTHORIZED);			 
		}
		
		return ar;
	}	

	@Override
	public AuthResponse checkEmail(LoginData ld) {
		AuthResponse ar = new AuthResponse();
		StatusResponse sr = new StatusResponse();
		AuthInfo ai = new AuthInfo();
		UserAccountsAuth uaa;
		DataResponse dr = new DataResponse();

		uaa = (UserAccountsAuth) uaar.findByEmail(ld.getEmail());
		ai.setEmail(uaa.getEmail());
		ai.setUserLevel(uaa.getUserlevel());
		
	    dr.setUserinfo(ai);
	    ar.setData(dr);
	    ar.setStatus(sr);
		
		return ar;		
	}

	@Override
	public AuthResponse checkLevel(int userlevel, AuthResponse ar) {
		StatusResponse sr = new StatusResponse();
		AuthInfo ai = new AuthInfo();
		DataResponse dr = new DataResponse();
		switch (userlevel)
		{
			case   1:sr.setMessage("user – access permission allowed");	
						sr.setValid(false);
						sr.setStatusCode(1);						 
						break;			
			case   8:sr.setMessage("subadmin with is slightly limited access (allowed access to all company accounts)");
						sr.setValid(false);
						sr.setStatusCode(1);						 
						break;	
			case   9:sr.setMessage("admin which is the Super Admin or Master Admin level with 100% access");	
						sr.setValid(false);
						sr.setStatusCode(1);						 
						break; 		
			case  -1:sr.setMessage("user account locked- no access");
						sr.setValid(false);			
						sr.setStatusCode(0);						 
						break;
			case  -5:sr.setMessage("T&Cs not yet accepted");
						sr.setValid(false);			
						sr.setStatusCode(0);						 
						break;
			case -10:sr.setMessage("Email not verified");
					    sr.setValid(false);			
					    sr.setStatusCode(0);						 
					    break;  
 			 default:sr.setMessage("Undefined User Level");	
 			 			sr.setValid(false); 			 
 					    sr.setStatusCode(9);
 					    break;
		}
	    dr.setUserinfo(ai);
	    ar.setData(dr);
		ar.setStatus(sr);
		
		return ar;
	}

	@Override
	public AuthResponse checkPassword(LoginData ld, UserAccountsAuth uaa, AuthResponse ar) {

		AuthInfo ai = new AuthInfo();
		StatusResponse sr = new StatusResponse();
		DataResponse dr = new DataResponse();
			
		if(comparePassword(ld.getPassword(),uaa.getPassword()))	
		{
			ai.setUserid(uaa.getUserid());
			ai.setRegnum(uaa.getRegnum()); 
			ai.setUseraccountid(uaa.getUseraccountid());
			ai.setUserLevel(uaa.getUserlevel());
			ai.setEmail(uaa.getEmail());
			sr.setValid(true);
			sr.setStatusCode(2);      // 2 -  if password matched
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(3);     // 3 -  if password not matched
			sr.setMessage("The email/password provided does not match in our record");
		}

	    dr.setUserinfo(ai);
	    ar.setData(dr);
	    ar.setStatus(sr);		
			
		return ar;
	}	
	
	public String getHash(String plainPassword)
	{
		 int strength = 10; // work factor of bcrypt
		 BCryptPasswordEncoder bCryptPasswordEncoder =
		  new BCryptPasswordEncoder(strength, new SecureRandom());
		 String encodedPassword = bCryptPasswordEncoder.encode(plainPassword);
		 
		 return encodedPassword;
	}
	
	public boolean comparePassword(String textPassword,String dbPassword)
	{
		boolean passChecker = false;
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		passChecker = bc.matches(textPassword,dbPassword);
		
		return passChecker;
	}
	
	public String generateUserid(Long useraccountid)
	{
		String encriptId = "";
		
		String salt = UniqueKeyGeneratorWithSalt.generateRandomSalt();

        encriptId = UniqueKeyGeneratorWithSalt.generateUniqueKey(useraccountid, salt);		
		
		return encriptId;
	}	
	
	public static void main(String [] args)
	{
		AuthServiceImpl asi = new AuthServiceImpl();
//		System.out.println(asi.checkUser(211804));
		System.out.println(asi.generateUserid(211804L));
	}
}