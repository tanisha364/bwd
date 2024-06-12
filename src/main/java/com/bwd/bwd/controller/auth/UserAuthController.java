package com.bwd.bwd.controller.auth;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.model.auth.AccountRequest;
import com.bwd.bwd.model.auth.OauthClients;
import com.bwd.bwd.model.auth.PasswordHistory;
import com.bwd.bwd.model.auth.QuestionAssignmentAuth;
import com.bwd.bwd.model.auth.RegistrationActivity;
import com.bwd.bwd.model.auth.UesrTokenAuth;
import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.model.auth.UserAssociationAuth;
import com.bwd.bwd.model.auth.UserEmailsAuth;
import com.bwd.bwd.model.auth.UserTelsAuth;
import com.bwd.bwd.model.jobsmith.UserEmails;
import com.bwd.bwd.repository.OauthClientsRepo;
import com.bwd.bwd.repository.PasswordHistoryRepo;
import com.bwd.bwd.repository.QuestionAssignmentAuthRepo;
import com.bwd.bwd.repository.RegistrationActivityRepo;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.repository.UserAssociationAuthRepo;
import com.bwd.bwd.repository.UserEmailAuthRepo;
import com.bwd.bwd.repository.UserTelAuthRepo;
import com.bwd.bwd.repository.UserTokenAuthRepo;
import com.bwd.bwd.request.LoginData;
import com.bwd.bwd.request.TokenInfoReq;
import com.bwd.bwd.response.AssignmentResponse;
import com.bwd.bwd.response.AuthInfo;
import com.bwd.bwd.response.AuthResponse;
import com.bwd.bwd.response.AuthTokenResponse;
import com.bwd.bwd.response.DataResponse;
import com.bwd.bwd.response.EmailResponse;
import com.bwd.bwd.response.RegDataResponse;
import com.bwd.bwd.response.RegInfo;
import com.bwd.bwd.response.RegPageResponse;
import com.bwd.bwd.response.RegTextResponse;
import com.bwd.bwd.response.RegisterResponse;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TermsResponse;
import com.bwd.bwd.response.TextResponse;
import com.bwd.bwd.response.TokenInfo;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.UserTokenInfo;
import com.bwd.bwd.response.UserTokenResponse;
import com.bwd.bwd.service.AuthServices;
import com.bwd.bwd.serviceimpl.AuthServiceImpl;
import com.bwd.bwd.serviceimpl.Base64JsonServiceImpl;
import com.bwd.bwd.serviceimpl.JWTServiceImpl;
import com.bwd.bwd.serviceimpl.JwtUserToken;
import com.bwd.bwd.serviceimpl.UserInfoImpl;
import com.bwd.bwd.util.DateTimeCreation;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@CrossOrigin("*")
@RequestMapping(path = "/auth", produces = "application/json")
@RestController

public class UserAuthController {

	String tokenType = "Bearer";

	@Autowired
	OauthClientsRepo ocr;

	@Autowired
	UserAccountsAuthRepo uaar;

	@Autowired
	UserEmailAuthRepo uer;

	@Autowired
	UserTelAuthRepo utr;

	@Autowired
	UserTokenAuthRepo utor;

	@Autowired
	UserAssociationAuthRepo uacar;

	@Autowired
	QuestionAssignmentAuthRepo qaar;
	
	@Autowired
	RegistrationActivityRepo rap;
	
	@Autowired
	PasswordHistoryRepo phr;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${image.file.name}")
	private String imageFileName;

	@Value("${link.url}")
	private String linkurl;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Value("${spring.mail.display.name}")
	private String senderDisplayName;


	@GetMapping("/token")
	public ResponseEntity<TokenResponse> generateBasicToken(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		ResponseEntity<TokenResponse> entity = null;

		String[] parts = authorizationHeader.split(" ");

		String tokenType = parts[0];

		if (parts.length == 2 && tokenType.equals("Basic")) {

			String pubicKey = parts[1];

			Base64JsonServiceImpl bjsi = new Base64JsonServiceImpl();

			String jsonObject = bjsi.base64DecodeJson(pubicKey);

			System.out.println(jsonObject);

			OauthClients oc = bjsi.getObject(jsonObject);

			entity = generateToken(oc, "Bearer");
		}
		return entity;
	}

	@PostMapping("/validate/user")
	public ResponseEntity<AuthTokenResponse> validateUserWithToken(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody LoginData data) {

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
		 * StatusCode - 0 : Unauthentic User with Unauthentic Token StatusCode - 1 :
		 * Authentic User with Authentic Token valid - true StatusCode - 2 : Authentic
		 * User with Expired Token StatusCode - 3 : Unauthentic User with Authentic
		 * Token StatusCode - 4 : Unauthentic User with Expired Token
		 * 
		 */

		if (validToken) {
			if (validAuth) {
				sr.setValid(true);
				sr.setStatusCode(1);
				isUpdateToken = true;
				sr.setMessage("Authentic User with Authentic Token");
			} else {
				sr.setValid(false);
				sr.setStatusCode(3);
				sr.setMessage("Unauthentic User with Authentic Token");
			}
		} else {
			sr.setValid(false);
			if (validAuth) {
				if (statuscodeToken == 2) {
					sr.setMessage("Authentic User with Expired Token");
					ar = null;

					sr.setStatusCode(2);
				} else {
					sr.setMessage("Authentic User with Unauthentic Token");
					sr.setStatusCode(3);
				}
			} else {
				if (statuscodeToken == 2) {
					sr.setMessage("Unauthentic User with Expired Token");
					sr.setStatusCode(4);
				} else {
					sr.setMessage("Unauthentic User with Unauthentic Token");
					sr.setStatusCode(0);
				}
			}
		}

		if (isUpdateToken) {
			JwtUserToken jut = new JwtUserToken();
			UserEmails userEmails = new UserEmails();
			userEmails.setEmail(ai.getEmail());
			userEmails.setUseraccountid(ai.getUseraccountid());

			String jwtToken = jut.generateToken(userEmails);
			String refreshToken = jut.refreshToken(jwtToken);

			String useridToken = jwtToken;

			UserAccountsAuth user = uaar.getReferenceById(ai.getUseraccountid());
			user.setLastvisit(DateTimeCreation.getModifedTimestamp());
			user.setUserid(useridToken);
			user.setRefreshtoken(refreshToken);
			uaar.save(user);

			ai.setEmail(data.getEmail());
			ai.setUserid(user.getUserid());
			ai.setRefreshtoken(user.getRefreshtoken());
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
		if (tokenType.endsWith("Bearer") || tokenType.equals("Basic")) {
			validTokenTypes = true;
		}
		if (parts.length == 2 && validTokenTypes) {
			token = parts[1];
			// Now you have the token, you can use it as needed
			TokenInfo ti = null;
			TokenInfoReq tir = null;

			//			ti = jsi.parseToken(token,tokenType);
			tir = jsi.parseToken(token, tokenType);
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
		System.out.println("RKA " + data);
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
	public long getUserAccountId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			@RequestHeader(HttpHeaders.USER_AGENT) String data) {
		long useraccountid = -1l;

		return useraccountid;
	}

	/*
	 * private long fetchUserAccountId(UserData data) { long useraccountid = -1l;
	 * UserAccountsAuth uaa = new UserAccountsAuth();
	 * 
	 * try { uaa = uaar.getReferenceByUserid(data.getUserid()); useraccountid =
	 * uaa.getUseraccountid(); } catch (Exception ex) { ex.printStackTrace();
	 * System.out.println(ex.getMessage()); } return useraccountid; }
	 */

	@PostMapping("/refreshtoken")
	public ResponseEntity<UserTokenResponse> generateRefreshToken(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			@RequestBody UserAccountsAuth userAccountsAuth) {
		ResponseEntity<UserTokenResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		JwtUserToken jut = new JwtUserToken();

		UserTokenResponse utr = new UserTokenResponse();
		UserTokenInfo uti = new UserTokenInfo();
		StatusResponse sr = new StatusResponse();

		String refreshtoken = userAccountsAuth.getRefreshtoken();

		System.out.println(refreshtoken);

		UserEmails userEmails = new UserEmails();

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {
				userEmails = jut.getUserEmailClaims(refreshtoken);

				String jwtToken = jut.generateToken(userEmails);
				String refreshToken = jut.refreshToken(jwtToken);

				UserAccountsAuth user = uaar.getReferenceById(userEmails.getUseraccountid());
				user.setUserid(jwtToken);
				user.setRefreshtoken(refreshToken);
				uaar.save(user);

				uti.setUserToken(jwtToken);
				uti.setRefreshToken(refreshToken);

				sr.setMessage("JWT token refreshed  successfully");
				sr.setValid(true);
				sr.setStatusCode(1);

				entity = new ResponseEntity<>(utr, headers, HttpStatus.OK);

			} catch (io.jsonwebtoken.ExpiredJwtException ejex) {
				entity = new ResponseEntity<>(utr, headers, HttpStatus.UNAUTHORIZED);
				System.out.println("JWT Refresh token expired : " + ejex.getMessage());
				sr.setMessage("JWT Refresh token expired");
				sr.setValid(false);
				sr.setStatusCode(22);
			} catch (Exception ejex) {
				entity = new ResponseEntity<>(utr, headers, HttpStatus.UNAUTHORIZED);
				System.out.println("Exception Occured OR Wrong Refresh Token : " + ejex.getMessage());
				sr.setMessage("Exception Occured OR Wrong Refresh Token");
				sr.setValid(false);
				sr.setStatusCode(23);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");
		}

		utr.setStatus(sr);
		utr.setUserToken(uti);
		return entity;
	}

	public boolean checkToken(String authorizationHeader) {
		boolean validToken = false;

		TokenResponse tr = new TokenResponse();
		ResponseEntity<TokenResponse> entityToken = null;

		entityToken = validateBearerToken(authorizationHeader);

		tr = entityToken.getBody();
		StatusResponse srToken = tr.getStatus();
		validToken = srToken.isValid();

		return validToken;
	}

	@PostMapping("/register/user")
	public ResponseEntity<RegisterResponse> registerUser(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			@RequestBody AccountRequest userAccount) {
		String message = "";
		ResponseEntity<RegisterResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		RegisterResponse rr = new RegisterResponse();
		StatusResponse sr = new StatusResponse();

		RegInfo ui = new RegInfo();
		RegDataResponse rdr = new RegDataResponse();
		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {
				
				String emailQuery = "SELECT useraccountid FROM user_email_tbl WHERE email = ? LIMIT 1";								
				List<Map<String, Object>> userAccountIdData = jdbcTemplate.queryForList(emailQuery, userAccount.getEmail());
				long Euid = 0;
				if (!userAccountIdData.isEmpty()) {
				    Euid = (long) userAccountIdData.get(0).get("useraccountid");
				}
				System.out.println(Euid);

				String telQuery = "SELECT useraccountid FROM user_tel_tbl WHERE tel = ? LIMIT 1";								
				List<Map<String, Object>> userTelData = jdbcTemplate.queryForList(telQuery, userAccount.getTel());
				long Tuid = 0;
				if (!userTelData.isEmpty()) {
				    Tuid = (long) userTelData.get(0).get("useraccountid");
				}
				System.out.println(Tuid);	
					
				if(Euid == Tuid && Euid != 0) {
					
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");			
					  String link = "SELECT linkid FROM user_accounts WHERE useraccountid = ?" ;
					  String Link = jdbcTemplate.queryForObject(link, String.class, Euid);
					  
					    ui.setLinkid(Link);
						rdr.setUserinfo(ui);
						rr.setData(rdr);

						message = "User already registerd";

						sr.setValid(true);
						sr.setStatusCode(11);
						sr.setMessage(message);

						rr.setStatus(sr);
						entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);
				}
			
				else if (Tuid == 0 && Euid != 0) {
					
					System.out.println("???????????????????????????????");			
				    String link = "SELECT linkid FROM user_accounts WHERE useraccountid = ?";
				    String Link = jdbcTemplate.queryForObject(link, String.class, Euid);

				    ui.setLinkid(Link);
				    rdr.setUserinfo(ui);
				    rr.setData(rdr);

				    message = "Please verify to add Phone number";

				    sr.setValid(true);
				    sr.setStatusCode(12);
				    sr.setMessage(message);

				    rr.setStatus(sr);
				    entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);
				}
				
				else if (Euid != Tuid && Euid != 0) {
					
					System.out.println(")))))))))))))))))))))))))))");			
					 String link = "SELECT linkid FROM user_accounts WHERE useraccountid = ?";
					    String Link = jdbcTemplate.queryForObject(link, String.class, Euid);

					    ui.setLinkid(Link);
					    rdr.setUserinfo(ui);
					    rr.setData(rdr);

					    message = "Email and Phone number are registered with different account";

					    sr.setValid(true);
					    sr.setStatusCode(13);
					    sr.setMessage(message);

					    rr.setStatus(sr);
					    entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);
				}
				
				else if (Euid == 0 && Tuid != 0) {
					
					System.out.println("++++++++++++++++++++++++++++++s");			
					
					 String link = "SELECT linkid FROM user_accounts WHERE useraccountid = ?";
					    String Link = jdbcTemplate.queryForObject(link, String.class, Tuid);

					    ui.setLinkid(Link);
					    rdr.setUserinfo(ui);
					    rr.setData(rdr);

					    message = "Phone number are registered with different account";

					    sr.setValid(true);
					    sr.setStatusCode(14);
					    sr.setMessage(message);

					    rr.setStatus(sr);
					    entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);
				}
				
				else {
			
					String linkid1 = UserInfoImpl.generateUniqueLinkId();					

					String verificationid = UserInfoImpl.generateUniqueLinkId(30);
					System.out.println("Token : " + verificationid);

					String sql1 = "select companyid from landingpage where code = '" + userAccount.getCode() + "' and landingid = " + userAccount.getLandingid();
					int companyid = jdbcTemplate.queryForObject(sql1, Integer.class);

					String sql = "select mark from company where companyid=" + companyid;
					String mark = jdbcTemplate.queryForObject(sql, String.class);

					String sql2 = "SELECT comptoken from landingpage where code = '" + userAccount.getCode() + "' and landingid = " + userAccount.getLandingid() + " and companyid=" + companyid;
					int comptokenid = jdbcTemplate.queryForObject(sql2, Integer.class);

					String sql3 ="SELECT jobid FROM job_tbl j INNER JOIN department_tbl dep ON j.departmentid = dep.departmentid WHERE companyid= "+companyid +" AND dep.subof = -1 LIMIT 1";
					int jobid = jdbcTemplate.queryForObject(sql3, Integer.class);

					String sql4 = "SELECT defaultoption from landingpage where code = '" + userAccount.getCode() + "' and landingid = " + userAccount.getLandingid() + " and companyid=" + companyid;
					int option = jdbcTemplate.queryForObject(sql4, Integer.class);

					UserAccountsAuth uaa = new UserAccountsAuth();
					uaar.save(uaa);
					Long useraccountid = uaa.getUseraccountid();
					String regnum = mark + useraccountid;

					uaar.save(uaa.createAccount(userAccount, regnum, linkid1, option));

					Long id = uaa.getUseraccountid();
					int convertedId = id.intValue();

					UserEmailsAuth uea = new UserEmailsAuth();
					uer.save(uea.createEmail(id, userAccount.getEmail(),verificationid));
					Long bwdEmailId = uea.getBwdEmailId();
					String email = uea.getEmail();

					UserTelsAuth uta = new UserTelsAuth();
					utr.save(uta.createTel(id, userAccount.getTel(), userAccount.getTelCode()));

					UesrTokenAuth utoa = new UesrTokenAuth();
					utor.save(utoa.createToken(convertedId, companyid, comptokenid));

					String companyemail = userAccount.getEmail();
					UserAssociationAuth uaca = new UserAssociationAuth();
					uacar.save(uaca.createAssociation(convertedId, comptokenid, companyid, jobid, companyemail,
							userAccount.getParticipanttype()));

					String sql6 = "select test_id, archived, squence from landing_questionnaire_tbl lqt INNER JOIN landingpage lp ON lqt.landing_assessment_id = lp.landingassessmentid where code = '" + userAccount.getCode() + "' and landingid = " + userAccount.getLandingid();

					List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql6);

					for (Map<String, Object> row : rows) {
						int testId = (int) row.get("test_id");
						int archived = (int) row.get("archived");
						int sequence = (int) row.get("squence");

						QuestionAssignmentAuth qaa = new QuestionAssignmentAuth();
						qaar.save(qaa.createAss(convertedId, companyid, sequence, testId, archived));
					}
					
					RegistrationActivity ra = new RegistrationActivity();
					rap.save(ra.registrationActivity(id, companyid, comptokenid, jobid, userAccount.getUseragent(), userAccount.getIp()));
					

					String generateLink =linkurl+verificationid+bwdEmailId+"-"+useraccountid ;


					MimeMessage mess = javaMailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mess);  
					try {
						helper.setFrom(sender, senderDisplayName);
						helper.setTo(email);
						helper.setSubject("Your BestWork DATA User Registration has been received");

						String emailText = "Welcome! You've just registered at BestWork DATA with the following information:"
								+ "  You will need to verify your email address before you can login to your account.  This is not required to complete the questionnaire. "
								+ "To verify your email address simply click the following link: (if you cannot click link, then copy and paste into your browser)\n" + generateLink;
						helper.setText(emailText);

						javaMailSender.send(mess);
					} catch (MessagingException | UnsupportedEncodingException e) {
						e.printStackTrace();
					} 

					ui.setLinkid(linkid1);
					rdr.setUserinfo(ui);
					rr.setData(rdr);

					message = "User Registered successfully";

					sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage(message);

					rr.setStatus(sr);
					entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);
				}
			}
			 catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				rr.setStatus(sr);
				entity = new ResponseEntity<>(rr, headers, HttpStatus.NOT_FOUND);
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

	@SuppressWarnings("deprecation")
	@PostMapping("/landingpage")
	public ResponseEntity<TextResponse> registerText(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			@RequestBody AccountRequest userAccount) {

		ResponseEntity<TextResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		TextResponse tr = new TextResponse();
		StatusResponse sr = new StatusResponse();
		RegPageResponse rr = new RegPageResponse();
		RegTextResponse rtr = new RegTextResponse();

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {

				String query1 = "select logo from landingpage where code = '" + userAccount.getCode()
				+ "' and landingid = " + userAccount.getLandingid();

				System.out.println(query1);
				jdbcTemplate.query(query1, new Object[] {}, rs -> {
					String logoFilename = rs.getString("logo");
					rtr.setLogo(imageFileName + logoFilename);
				});

				String query2 = "select text from pages where pageid =2 ";
				System.out.println(query2);
				jdbcTemplate.query(query2, new Object[] {}, rs -> {
					rtr.setText(rs.getString("text"));
				});

				String query3 = "select companyname from company c INNER JOIN landingpage lp ON c.companyid = lp.companyid where lp.code = '" + userAccount.getCode() + "' and lp.landingid = " + userAccount.getLandingid();
				System.out.println(query3);
				jdbcTemplate.query(query3, new Object[] {}, rs -> {
					rtr.setCompanyname(rs.getString("companyname"));
				});
				
				String query4 = "select text, metadescription from pages where pageid =67";
				System.out.println(query4);
				jdbcTemplate.query(query4, new Object[] {}, rs -> {
					rtr.setVideo(rs.getString("text"));
					rtr.setImage(rs.getString("metadescription"));
				});
				
				rr.setRt(rtr);
				sr.setValid(true);
				sr.setStatusCode(1);
				sr.setMessage("Registration page");

				tr.setStatus(sr);
				tr.setData(rr);
				entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);
			} catch (Exception ex) {
				ex.printStackTrace();
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				tr.setData(null);
				tr.setStatus(sr);
				entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
			return entity;
		}
		return entity;
	}

	@PostMapping("/emailverification")
	public ResponseEntity<StatusResponse> verification(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Map<String, Object> requestBody) {

		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();		

		String verificationId = (String) requestBody.get("verificationId");
		String mailId = (String) requestBody.get("mailId");
		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {
				String ver = "SELECT * FROM user_email_tbl WHERE bwd_email_id = ? AND verificationid = ?";

				List<Map<String, Object>> resultList = jdbcTemplate.queryForList(ver, mailId, verificationId);

				if (!resultList.isEmpty()) { 

					String updateQuery1 = "UPDATE user_email_tbl SET verificationid = NULL, date_verified = UTC_TIMESTAMP WHERE bwd_email_id = ? AND verificationid = ?";
					jdbcTemplate.update(updateQuery1, mailId, verificationId);                 

					String updateQuery2 = "UPDATE user_accounts SET userlevel = -5 WHERE useraccountid = (SELECT useraccountid FROM user_email_tbl WHERE bwd_email_id = ?)";
					jdbcTemplate.update(updateQuery2, mailId);

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  
					entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);        
				} else {
					sr.setValid(false);    
					sr.setStatusCode(3);
					sr.setMessage("No record found");  
					entity = new ResponseEntity<>(sr, headers, HttpStatus.BAD_REQUEST);  
				}
			} catch(NullPointerException npex) {

				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   		        
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
	
	
	@PostMapping("/terms&conditionaccept")
	public ResponseEntity<TermsResponse> conditionaccept(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,  @RequestBody Map<String, String> requestBody) {

		ResponseEntity<TermsResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		TermsResponse tr = new TermsResponse();
		StatusResponse sr = new StatusResponse();		

		String mailId = requestBody.get("mailId");

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {
				       
				String emailid = "select email from user_email_tbl WHERE bwd_email_id = ? ";
				String email = jdbcTemplate.queryForObject(emailid, String.class, mailId);
				
					String updateQuery = "UPDATE user_accounts SET userlevel = 1 WHERE useraccountid = (SELECT useraccountid FROM user_email_tbl WHERE bwd_email_id = ?)";
					jdbcTemplate.update(updateQuery, mailId);

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  
					
					tr.setStatus(sr);
					tr.setEmail(email);
										
					entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);        				
			} catch(NullPointerException npex) {

				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   		        
				entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);    
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");
			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);    
		} 
		return entity;
	}
	
	
	@PostMapping("/crypticpasskeygen")
	public ResponseEntity<StatusResponse> passwordgenerate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Map<String, Object> requestBody) {

		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();		

		String password = (String) requestBody.get("password");
		String mailId = (String) requestBody.get("mailId");
		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {

				AuthServiceImpl asi = new AuthServiceImpl();				
				String password1 = asi.getHash(password);

				String uid = "SELECT useraccountid FROM user_email_tbl WHERE bwd_email_id = "+mailId;
				List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(uid);	         	
				Long UID = accountIdData.isEmpty() ? 0L : (Long) accountIdData.get(0).get("useraccountid");								

				String count = "SELECT password_history_id FROM  password_history_tbl WHERE useraccountid=? AND archived=0";					
				int emailCount = 0; 
				try {
					emailCount = jdbcTemplate.queryForObject(count, Integer.class, UID);
				} catch (EmptyResultDataAccessException e) {	   
					emailCount = 0; 
				}       									

				if(emailCount > 0)
				{
					String last = "select password_history_id FROM password_history_tbl WHERE useraccountid=? AND last_3 = 1";
					List<Integer> passwordHistoryIds = jdbcTemplate.queryForList(last, Integer.class, UID);
					if (passwordHistoryIds.size() >= 3) {
						int smallestPasswordHistoryId = Collections.min(passwordHistoryIds);

						String up = "update password_history_tbl set last_3 = 0 where password_history_id = ? ";
						jdbcTemplate.update(up, smallestPasswordHistoryId);													
					}

					boolean passwordMatchesLastThree = comparePasswordWithLastThree(password, UID);					 					 
					if (passwordMatchesLastThree) {
						sr.setValid(false);
						sr.setStatusCode(11);
						sr.setMessage("Password matches one of the last three used passwords.");
						return new ResponseEntity<>(sr, headers, HttpStatus.BAD_REQUEST);
					}

					String pid = "SELECT password_history_id FROM  password_history_tbl WHERE useraccountid=? AND archived=0";
					int Passid = jdbcTemplate.queryForObject(pid, Integer.class, UID);

					String up = "UPDATE password_history_tbl SET archived=1 WHERE password_history_id=?";
					jdbcTemplate.update(up, Passid); 
				}

				String pwd = "UPDATE user_accounts SET password = ? WHERE useraccountid = (SELECT useraccountid FROM user_email_tbl WHERE bwd_email_id = ?) ";
				jdbcTemplate.update(pwd, password1, mailId);  

				PasswordHistory ph = new PasswordHistory();
				phr.save(ph.createPasswordHistory(UID,password1));

				sr.setValid(true);    
				sr.setStatusCode(1);
				sr.setMessage("Authenticate User Success");  
				entity = new ResponseEntity<>(sr, headers, HttpStatus.OK); 
			}catch(NullPointerException npex) {

				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   		        
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


	public boolean comparePassword(String textPassword,String dbPassword)
	{
		boolean passChecker = false;
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		passChecker = bc.matches(textPassword,dbPassword);

		return passChecker;
	}

	@SuppressWarnings("deprecation")
	public boolean comparePasswordWithLastThree(String textPassword, Long UID) {
		String query = "SELECT ph.password FROM password_history_tbl ph WHERE ph.useraccountid = ? ORDER BY ph.date_set DESC LIMIT 3";
		List<String> lastThreePasswords = jdbcTemplate.queryForList(query, new Object[]{UID}, String.class);
		for (String dbPassword : lastThreePasswords) {
			if (comparePassword(textPassword, dbPassword)) {
				return true;
			}
		}

		return false;
	}

	
	
	
	@PostMapping("/isemailvalid")
	public ResponseEntity<EmailResponse> isemailexist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,  @RequestBody Map<String, String> requestBody) {

		ResponseEntity<EmailResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();

		EmailResponse tr = new EmailResponse();
		StatusResponse sr = new StatusResponse();		

		String email = requestBody.get("email");

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {
				String sqlCount = "SELECT COUNT(*) FROM user_email_tbl WHERE email = ?";
	            int emailCount = jdbcTemplate.queryForObject(sqlCount, Integer.class, email);
				
				if(emailCount > 0)
				{
					
				String sql1 = "SELECT useraccountid FROM user_email_tbl WHERE email = ? ";
				long useraccountid = jdbcTemplate.queryForObject(sql1, Integer.class, email);
				
				String sql2 = "select userlevel from user_accounts WHERE useraccountid = ? ";
				int userlevel = jdbcTemplate.queryForObject(sql2, Integer.class, useraccountid);
				
				String sql3 = "SELECT IFNULL((SELECT tel FROM user_tel_tbl WHERE useraccountid = ? LIMIT 1), '0') AS tel";
				String tel = jdbcTemplate.queryForObject(sql3, String.class, useraccountid);

				if (tel == null || tel.equals("0")) {
                	tel = "";
                }
				
				String sql4 = "SELECT tel_code FROM user_tel_tbl WHERE useraccountid = ? LIMIT 1";				
				
				int tel_code = 0; 
				try {
					tel_code = jdbcTemplate.queryForObject(sql4, Integer.class, useraccountid);
				} catch (EmptyResultDataAccessException e) {	   
					tel_code = 0; 
				}  
				
				String sql = "select bwd_email_id from user_email_tbl WHERE email = ? ";
				int mailId = jdbcTemplate.queryForObject(sql, Integer.class, email);
									
				
					if(userlevel == -10 || userlevel == -5) {
						sr.setValid(false);    
						sr.setStatusCode(2);
						sr.setMessage("Email not verified");  
						
						tr.setStatus(sr);
						tr.setEmail(email);
						tr.setEmailId((long) mailId);
						entity = new ResponseEntity<>(tr, headers, HttpStatus.FORBIDDEN);
					}
					else {
					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  
					
					tr.setStatus(sr);
					tr.setEmail(email);
					tr.setEmailId((long) mailId);
					tr.setPhonenumber(tel);
					tr.setTel_code(tel_code);
					entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);
					}
			}
				
				else {
					sr.setValid(false);    
					sr.setStatusCode(11);
					sr.setMessage("Email not found");  
					
					tr.setStatus(sr);
					entity = new ResponseEntity<>(tr, headers, HttpStatus.NOT_FOUND);
				}
				}
			 catch(NullPointerException npex) {

				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   		        
				entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);    
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");
			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);    
		} 
		return entity;
	}
	
	
	@PostMapping("/mailverifylinkgenerate")
	public ResponseEntity<EmailResponse> mailverifylinkgenerate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,  @RequestBody Map<String, String> requestBody) {

		ResponseEntity<EmailResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();

		EmailResponse tr = new EmailResponse();
		StatusResponse sr = new StatusResponse();		

		String mailId = requestBody.get("mailId");

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");
			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);    
		} 
		return entity;
	}
	
	@PostMapping("/assignmentcheck")
	public ResponseEntity<AssignmentResponse> assignmentcheck(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,  @RequestBody Map<String, String> requestBody) {

		ResponseEntity<AssignmentResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();

		AssignmentResponse tr = new AssignmentResponse();
		StatusResponse sr = new StatusResponse();		

		String linkid = requestBody.get("linkid");
		String landingid = requestBody.get("landingid");

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if (validToken) {
			try {
				
				
				String sql = "SELECT useraccountid FROM user_accounts WHERE linkid = '" + linkid + "'";
				List<Map<String, Object>> ua = jdbcTemplate.queryForList(sql);	         	
				Long UID = ua.isEmpty() ? 0L : (Long) ua.get(0).get("useraccountid");								
				int convertedId = UID.intValue();
				
				String sql1 = "select test_id from questionnaire_assignment where useraccountid = " +convertedId;
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql1);
				List<Integer> testIds = new ArrayList<>();
				for (Map<String, Object> row : rows) {
				    Integer testId = (Integer) row.get("test_id");
				    testIds.add(testId);
				}
					
				
				String sql2 = "select landingassessmentid from landingpage WHERE landingid =  ?";
				int lan = jdbcTemplate.queryForObject(sql2, Integer.class, landingid);			
								
				
				String sql3 = "select test_id from landing_questionnaire_tbl where landing_assessment_id = " +lan + " and add_after_existing = 1 ";
                List<Map<String, Object>> check = jdbcTemplate.queryForList(sql3);
				List<Integer> testids = new ArrayList<>();
				for (Map<String, Object> row : check) {
				    Integer testId = (Integer) row.get("test_id");
				    testids.add(testId);
				}
				
				
				String sql4 = "select companyid from landingpage WHERE landingid =  ?";
				int companyid = jdbcTemplate.queryForObject(sql4, Integer.class, landingid);	
				
				
				Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
				int sequence = testIds.size() + 1;
				
				String insertSql = "INSERT INTO questionnaire_assignment (useraccountid, assigned_by, date_assigned, test_id, sequence) VALUES (?, ?, ?, ?, ? )";
				for (Integer testId : testids) {
				    if (!testIds.contains(testId)) {
				        jdbcTemplate.update(insertSql, convertedId, companyid, currentTimestamp, testId, sequence);
				    }
				}
				
				
				String ver = "SELECT * FROM questionnaire_assignment WHERE useraccountid = ? AND archive = 0";
				List<Map<String, Object>> resultList = jdbcTemplate.queryForList(ver, convertedId);

				if (!resultList.isEmpty()) {
					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Complete all assignments");  
					
					tr.setStatus(sr);
					tr.setPending(true);
					entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);
				}
				else {
					sr.setValid(true);    
					sr.setStatusCode(2);
					sr.setMessage("No assignments");  
					
					tr.setStatus(sr);
					tr.setPending(false);
					entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);
				}
									
			}
			
			 catch(NullPointerException npex) {

				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   		        
				entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);    
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");
			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);    
		} 
		return entity;
	}

}