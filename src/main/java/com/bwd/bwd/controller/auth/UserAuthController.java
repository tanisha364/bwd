package com.bwd.bwd.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.model.auth.OauthClients;
import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.model.auth.UserEmailsAuth;
import com.bwd.bwd.repository.OauthClientsRepo;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.repository.UserEmailAuthRepo;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.request.TokenInfoReq;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.AuthInfo;
import com.bwd.bwd.response.AuthResponse;
import com.bwd.bwd.response.AuthTokenResponse;
import com.bwd.bwd.response.DataResponse;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenInfo;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.service.AuthServices;
import com.bwd.bwd.serviceimpl.AuthServiceImpl;
import com.bwd.bwd.serviceimpl.Base64JsonServiceImpl;
import com.bwd.bwd.serviceimpl.JWTServiceImpl;

import io.jsonwebtoken.Claims;

@CrossOrigin("*")
@RequestMapping(path = "/auth", produces = "application/json")
@RestController

public class UserAuthController {
	
	String 	tokenType = "Bearer";
	
	@Autowired
	OauthClientsRepo ocr;	
	
	@Autowired
	UserAccountsAuthRepo uaar;	
	
	@Autowired
	UserEmailAuthRepo uer;	
	
	@GetMapping("/token")
	public ResponseEntity<TokenResponse> generateBasicToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		ResponseEntity<TokenResponse> entity = null;
		
		String[] parts = authorizationHeader.split(" ");

		String tokenType = parts[0];

		if (parts.length == 2 && tokenType.equals("Basic")) {

			String pubicKey = parts[1];

			Base64JsonServiceImpl bjsi = new Base64JsonServiceImpl();

			String jsonObject = bjsi.base64DecodeJson(pubicKey);

			System.out.println(jsonObject);

			OauthClients oc = bjsi.getObject(jsonObject);

			entity = generateToken(oc,"Bearer");
		}
		return entity;		
	}		
	
	@PostMapping("/validate/user")
	public ResponseEntity<AuthTokenResponse> validateUserWithToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			@RequestBody LoginData data) {

		AuthTokenResponse atr = new AuthTokenResponse();		
		StatusResponse sr = new StatusResponse();
		TokenResponse tr = new TokenResponse();
		AuthResponse ar = new AuthResponse();
		AuthInfo ai = new AuthInfo();
		
		ResponseEntity<AuthTokenResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		
		ResponseEntity<TokenResponse> entityToken = null;
		ResponseEntity<AuthResponse> entityAuth = null;
		
		entityToken = validateBearerToken(authorizationHeader);
		
		tr = entityToken.getBody();		
		StatusResponse srToken = tr.getStatus();		
		boolean validToken = srToken.isValid();
		int statuscodeToken = srToken.getStatusCode();
		
		entityAuth = validateUser(data);	
		ar = entityAuth.getBody();
		DataResponse dr = ar.getData();
		ai = dr.getUserinfo();
		StatusResponse srAuth = ar.getStatus();		
		boolean validAuth = srAuth.isValid();	
		boolean isUpdateToken = false;
	/*		 
		 StatusCode - 0    :    Unauthentic User with Unauthentic Token
		 StatusCode - 1    :    Authentic User with Authentic Token		valid - true
		 StatusCode - 2    :    Authentic User with Expired Token
		 StatusCode - 3    :    Unauthentic User with Authentic Token
		 StatusCode - 4    :    Unauthentic User with Expired Token
		 		 
	*/
		
		if(validToken)
		{			
			if(validAuth)
			{
				sr.setValid(true);
				sr.setStatusCode(1);
				isUpdateToken = true;
				sr.setMessage("Authentic User with Authentic Token");
			}
			else
			{
				sr.setValid(false);
				sr.setStatusCode(3);
				sr.setMessage("Unauthentic User with Authentic Token");				
			}
		}
		else
		{	
			sr.setValid(false);	
			if(validAuth)
			{	
				if(statuscodeToken==2)
				{
					sr.setMessage("Authentic User with Expired Token");
					ar = null;
					
					sr.setStatusCode(2);
				}
				else
				{
					sr.setMessage("Authentic User with Unauthentic Token");
					sr.setStatusCode(3);
				}
			}
			else
			{
				if(statuscodeToken==2)
				{
					sr.setMessage("Unauthentic User with Expired Token");
					sr.setStatusCode(4);
				}
				else
				{
					sr.setMessage("Unauthentic User with Unauthentic Token");
					sr.setStatusCode(0);
				}
			}			
		}	
		
		if(isUpdateToken)
		{
			AuthServiceImpl as = new AuthServiceImpl();
			String useridToken = as.generateUserid(ai.getUseraccountid());
			UserAccountsAuth user = uaar.getReferenceById(ai.getUseraccountid()); //= as.updateUser(ai.getUseraccountid(), useridToken);
			 user.setUserid(useridToken);
			 // Save the updated user back to the database
	        uaar.save(user);
			ai.setUserid(user.getUserid());
			dr.setUserinfo(ai);
			ar.setData(dr);
			System.out.println(useridToken);
		}
		
		atr.setAuthData(ar);
		atr.setStatus(sr);
		
		entity = new ResponseEntity<>(atr, headers, HttpStatus.OK);
		
		return entity;		
	}

	public ResponseEntity<TokenResponse> generateToken(@RequestBody OauthClients data, String tokenType) {
		
		this.tokenType = tokenType;
		ResponseEntity<TokenResponse> entity = generateToken(data);

		return entity;
	}	
	
	public ResponseEntity<TokenResponse> generateToken(@RequestBody OauthClients data) {

		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<TokenResponse> entity = null;

		StatusResponse sr = new StatusResponse();
		TokenResponse tr = new TokenResponse();

		TokenInfo ti = new TokenInfo();


		JWTServiceImpl jsi = new JWTServiceImpl();
		UserDetails userDetails = data;

		Long found = 0L;
		found = validateRoute(data);
		System.out.println("&&&& " + found);
		if (found.equals(1l)) {
			String jwtToken = jsi.generateToken(userDetails);
			ti.setAuthToken(jwtToken);
			Claims claims = jsi.getAllClaimsFromToken(jwtToken);

			ti.setExp((Long) claims.get("exp"));
			ti.setTimestamp(claims.getIssuedAt());
			ti.setToken_type(tokenType);

			sr.setValid(true);
			sr.setStatusCode(1);
			sr.setMessage("Token Generated Successfully");

			tr.setData(ti);
			tr.setStatus(sr);

			headers.setBearerAuth(jwtToken);

			entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);
		} else {
			sr.setValid(false);
			sr.setStatusCode(0);
			ti.setAuthToken(null);
			sr.setMessage("Invalid Credential - Token not generated");
			tr.setData(ti);
			tr.setStatus(sr);
			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
		}

		return entity;
	}
	
	public Long validateRoute(@RequestBody OauthClients data) {
		Long found = 0L;
		found = ocr.isRecordExist(data.getUsername(), data.getPassword());
		System.out.println("fOUND = " + found + " - " + data.getUsername() + " - " + data.getPassword());
		return found;
	}	
	
	public ResponseEntity<TokenResponse> validateBearerToken(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		JWTServiceImpl jsi = new JWTServiceImpl();

		ResponseEntity<TokenResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();

		TokenResponse tr = new TokenResponse();
		StatusResponse sr = new StatusResponse();

		// Authorization header will contain the token in the form "Bearer <token>"
		String[] parts = authorizationHeader.split(" ");
		String token = "";
		String tokenType = parts[0];
		boolean validTokenTypes = false;
		if(tokenType.endsWith("Bearer") || tokenType.equals("Basic"))
		{
			validTokenTypes = true;
		}
		if (parts.length == 2 && validTokenTypes) {
			token = parts[1];
			// Now you have the token, you can use it as needed
			TokenInfo ti = null;
			TokenInfoReq tir = null;

//			ti = jsi.parseToken(token,tokenType);
			tir = jsi.parseToken(token,tokenType);
			long currentSystemTime = System.currentTimeMillis();
			if (currentSystemTime > tir.getExp()) {
				sr.setMessage("Token Expire");
				sr.setStatusCode(2);
				sr.setValid(false);
				tr.setData(new TokenInfo());
				tr.setStatus(sr);

				entity = new ResponseEntity<>(tr, headers, HttpStatus.REQUEST_TIMEOUT);
			} else {
				System.out.println(authorizationHeader + " --- " + token);
				String ci = tir.getClient_id();
				String cs = tir.getClient_secret();

				Long found = 0L;
				found = ocr.isRecordExist(ci, cs);

				if (found.equals(1l)) {
					sr.setMessage("Valid Token");
					sr.setStatusCode(1);
					sr.setValid(true);

					tr.setData(ti);
					tr.setStatus(sr);

					entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);
				} else {
					sr.setMessage("Invalid Token");
					sr.setStatusCode(0);
					sr.setValid(false);
					tr.setData(new TokenInfo());
					tr.setStatus(sr);

					entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
				}
			}

		} else {
			// Handle invalid or missing token
			sr.setMessage("Invalid or missing authorization token");
			sr.setStatusCode(0);
			sr.setValid(false);
			tr.setData(new TokenInfo());
			tr.setStatus(sr);
			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}

	public ResponseEntity<AuthResponse> validateUser(@RequestBody LoginData data) {
		AuthServices as = new AuthServiceImpl();
		UserAccountsAuth uaa = new UserAccountsAuth();
		UserEmailsAuth uea = new UserEmailsAuth();
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<AuthResponse> entity = null;
		AuthResponse ar = new AuthResponse();
		StatusResponse sr = new StatusResponse();
		DataResponse dr = new DataResponse();
		AuthInfo ai = new AuthInfo();

		try {
			uea = uer.findByEmail(data.getEmail());
			ai.setEmail(uea.getEmail());
			ai.setUseraccountid(uea.getUseraccountid());
			sr.setStatusCode(2);

			dr.setUserinfo(ai);
			ar.setData(dr);
			ar.setStatus(sr);

			try {
				uaa = uaar.getReferenceById(uea.getUseraccountid());
				try {
					ar = as.checkUser(uaa, ar);
					dr = ar.getData();
					ai = dr.getUserinfo();
					ar = as.checkLevel(ai.getUserLevel(), ar);
					System.out.println("------------------- ++++ : " + ai.getUserLevel());
					if (ai.getUserLevel() == -10) {
						ai.setUseraccountid(null);
						ai.setEmail(null);
						ai.setRegnum(null);
						ai.setUserLevel(0);
						sr.setValid(false);
						sr.setStatusCode(2);
						sr.setMessage("Email not verified");

						dr.setUserinfo(ai);
						ar.setData(dr);
						ar.setStatus(sr);
					} else {
						ar = as.checkPassword(data, uaa, ar);
						dr = ar.getData();
						ai = dr.getUserinfo();
						sr = ar.getStatus();
						if (ai.getUserLevel() == 1 || ai.getUserLevel() == 8 || ai.getUserLevel() == 9) {
							if (sr.getStatusCode() == 2) {
								sr.setValid(true);
								sr.setStatusCode(6);								
								sr.setMessage("Successful login");
							} else {
								ai.setUseraccountid(null);
								ai.setEmail(null);
								ai.setRegnum(null);
								ai.setUserLevel(0);
								sr.setValid(false);
								sr.setStatusCode(3);
								sr.setMessage("The email/password provided does not match in our record");
							}
							dr.setUserinfo(ai);
							ar.setData(dr);
							ar.setStatus(sr);
						}
						if (ai.getUserLevel() == -1) {
							ai.setUseraccountid(null);
							ai.setEmail(null);
							ai.setRegnum(null);
							ai.setUserLevel(0);
							if (sr.getStatusCode() == 2) {
								sr.setValid(false);
								sr.setStatusCode(4);
								sr.setMessage("user account locked- no access");
							} else {
								sr.setValid(false);
								sr.setStatusCode(7);
								sr.setMessage("The credential provided does not match in our record");
							}
						}
						if (ai.getUserLevel() == -5) {
							ai.setUseraccountid(null);
							ai.setEmail(null);
							ai.setRegnum(null);
							ai.setUserLevel(0);
							if (sr.getStatusCode() == 2) {
								sr.setValid(false);
								sr.setStatusCode(5);
								sr.setMessage("T&Cs not yet accepted");
							} else {
								sr.setValid(false);
								sr.setStatusCode(8);
								sr.setMessage("The credential provided does not match in our record");
							}
						}
					}

					dr.setUserinfo(ai);
					ar.setData(dr);
					ar.setStatus(sr);
					entity = new ResponseEntity<>(ar, headers, HttpStatus.OK);
				} catch (Exception e) {
					System.out.println("Some Error Occured\n" + e.getMessage());
					ai.setUseraccountid(null);
					ai.setEmail(null);
					ai.setRegnum(null);
					ai.setUserLevel(0);
					sr.setStatusCode(10);
					sr.setValid(false);
					sr.setMessage("Some Error Occured");

					dr.setUserinfo(ai);
					ar.setData(dr);
					ar.setStatus(sr);
					entity = new ResponseEntity<>(ar, headers, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception ex) {
				System.out.println(
						"The Useraccountid does not match with our record for provided email\n" + ex.getMessage());
				ai.setUseraccountid(null);
				ai.setEmail(null);
				ai.setRegnum(null);
				ai.setUserLevel(0);
				sr.setStatusCode(11);
				sr.setValid(false);
				sr.setMessage("The Useraccountid does not match with our record for provided email");

				dr.setUserinfo(ai);
				ar.setData(dr);
				ar.setStatus(sr);
				entity = new ResponseEntity<>(ar, headers, HttpStatus.NOT_FOUND);
			}
		} catch (Exception exp) {
			System.out.println("The email/password provided does not match our record \n" + exp.getMessage());
			ai.setUseraccountid(null);
			ai.setEmail(null);
			ai.setRegnum(null);
			ai.setUserLevel(0);
			sr.setStatusCode(0);
			sr.setValid(false);
			sr.setMessage("The email/password provided does not match our record");

			dr.setUserinfo(ai);
			ar.setData(dr);
			ar.setStatus(sr);
			entity = new ResponseEntity<>(ar, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
	
	@GetMapping("/getuseraccountid")
	public long getUserAccountId(@RequestHeader(HttpHeaders.USER_AGENT) String data) {
		long useraccountid = -1l;
		UserAccountsAuth uaa = new UserAccountsAuth();
		System.out.println("RKA "+data);
		try {	
			uaa = uaar.getReferenceByUserid(data);
			useraccountid = uaa.getUseraccountid();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		return useraccountid;
	}	
	
	@GetMapping("/getuseraccountidheader")
	public long getUserAccountId(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, 
			@RequestHeader(HttpHeaders.USER_AGENT) String data) {	
		long useraccountid = -1l;
		
		return useraccountid;
	}
	
	private long fetchUserAccountId(UserData data) {
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
}