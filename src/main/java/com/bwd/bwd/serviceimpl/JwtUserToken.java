 package com.bwd.bwd.serviceimpl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bwd.bwd.model.jobsmith.UserEmails;
import com.bwd.bwd.repository.OauthClientsRepo;
import com.bwd.bwd.request.TokenInfoReq;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtUserToken {	
	
	private String tokenType = "Bearer";
	@Autowired
	OauthClientsRepo ocr;

//	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY =  30 * 60 * 60;
	public static final long JWT_REFRESH_TOKEN_VALIDITY = 30  * 60 * 60;

//	@Value("${jwt.secret}")
	private String secret="9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9abcdefghijklmnOPQRSTUVWXYZ";	
	
	public Long isRecordExist(String useraccountid, String email)
	{		
		Long found = ocr.isRecordExist(useraccountid, email);
		return found;
	}
	
//	public String generateToken(String refreshToken) {
//		Map<String, Object> claims = new HashMap<>();
//		
//		return doGenerateToken(claims,userDetails.getEmail(),(userDetails.getUseraccountid())+"");
//	}	

	public String generateToken(UserEmails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims,userDetails.getEmail(),(userDetails.getUseraccountid())+"");
	}
	
	private String doGenerateToken(Map<String, Object> claims, String email,String useraccountid) {
		//.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
		final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + JWT_TOKEN_VALIDITY * 1000);		
		long expTime = System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000;
		return Jwts.builder().setClaims(claims).setClaims(claims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(expTime))	
				.claim("useraccountid", useraccountid)
				.claim("exp",expirationDate)
				.signWith((java.security.Key) getSiginKey(), SignatureAlgorithm.HS512) 
				.compact();
	}
	
	public TokenInfoReq parseToken(String jwtToken)
	{
		TokenInfoReq ti = new TokenInfoReq();
		
		Claims claims = getAllClaimsFromToken(jwtToken);
		
		ti.setClient_id(claims.getSubject());
		ti.setClient_secret((String)claims.get("useraccountid"));
	//	ti.setExpires_in(claims.getExpiration());
		ti.setExp((Long) claims.get("exp"));
		ti.setTimestamp(claims.getIssuedAt());	
		ti.setToken_type(tokenType);
		ti.setAuthToken(jwtToken);
		
		return ti;		
	}
	
	public TokenInfoReq parseToken(String jwtToken,String tokenType)
	{
		TokenInfoReq ti = new TokenInfoReq();
		this.tokenType = tokenType;
		
		ti = parseToken(jwtToken);
		
		return ti;	
	}
	
	private Key getSiginKey()
	{
		byte[] key = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(key);
	}

	public Claims getAllClaimsFromToken (String token) {
		return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody(); 
	}	
	
    public String refreshToken(String token) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + JWT_REFRESH_TOKEN_VALIDITY * 1000);
        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith((java.security.Key) getSiginKey(), SignatureAlgorithm.HS512) 
				.compact();
    }
    
    public UserEmails getUserEmailClaims(String token) {
    	UserEmails userEmails = new UserEmails();
    	Long uaid;
    	 
    	final Claims claims = getAllClaimsFromToken(token);
    	
    	userEmails.setEmail(claims.getSubject());
    	uaid = Long.parseLong(claims.get("useraccountid").toString());
    	userEmails.setUseraccountid(uaid);
        
    	return userEmails;
    }
    
    public boolean isValidAccessToken(String token)
    {
    	boolean isValid;
    	try
    	{
        	final Claims claims = getAllClaimsFromToken(token);
        	long curentTime = System.currentTimeMillis();
        	Date curentDate = new Date(curentTime); //getClaimFromToken(token, Claims::getExpiration);
        	if(curentDate.before(claims.getExpiration())){
        		isValid = true;
        	}
        	else
        	{
        		isValid = false;
        	}   		
    	}catch(io.jsonwebtoken.ExpiredJwtException ejex) {
    		isValid = false;
		}catch(Exception ejex) {
			isValid = false;
		}    	
    	return isValid;
    }
	
	public static void main(String [] args)
	{
		JwtUserToken jut = new JwtUserToken();
		UserEmails userEmails = new UserEmails();
		UserDetails userDetails = new UserEmails();
		
		userEmails.setUseraccountid(211804l);
		userEmails.setEmail("rajesha@gmail.com");
		
		userDetails = userEmails;
		
		System.out.println(userDetails);
		
		String jwtToken = jut.generateToken(userEmails);
		String refreshToken = jut.refreshToken(jwtToken);
		
		System.out.println("Access Token : "+jwtToken);
		System.out.println("Refresh Token : "+refreshToken);
		
		Claims claims = jut.getAllClaimsFromToken(jwtToken);

		System.out.println("---------------------- Token Claims -------------------------------");
		System.out.println(claims.getSubject());
		System.out.println(claims.get("useraccountid"));
		System.out.println(claims.getExpiration());
		System.out.println(claims.size());	

		Claims claimsRefresh = jut.getAllClaimsFromToken(refreshToken);
		
		System.out.println("---------------------- Refresh Token Claims -------------------------------");		
		System.out.println(claimsRefresh.getSubject());
		System.out.println(claimsRefresh.get("useraccountid"));
		System.out.println(claimsRefresh.getExpiration());
		System.out.println(claimsRefresh.size());	
	}	
}