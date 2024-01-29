 package com.bwd.bwd.serviceimpl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bwd.bwd.model.auth.OauthClients;
import com.bwd.bwd.repository.OauthClientsRepo;
import com.bwd.bwd.request.TokenInfoReq;
import com.bwd.bwd.response.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl{
	
	private String tokenType = "Bearer";
	@Autowired
	OauthClientsRepo ocr;

//	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 100 * 5 * 60;

//	@Value("${jwt.secret}")
	private String secret="9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9abcdefghijklmnOPQRSTUVWXYZ";	
	

	public Long isRecordExist(String client_id, String client_secret)
	{		
		Long found = ocr.isRecordExist(client_id, client_secret);
		return found;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername(),userDetails.getPassword());
	}
	
	private String doGenerateToken(Map<String, Object> claims, String clientId,String secretKey) {
		//.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
		long expTime = System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000;
		return Jwts.builder().setClaims(claims).setClaims(claims).setSubject(clientId).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(expTime))	
				.claim("secretKey", secretKey)
				.claim("exp",expTime)
				.signWith((java.security.Key) getSiginKey(), SignatureAlgorithm.HS512) 
				.compact();
	}
	
	public TokenInfoReq parseToken(String jwtToken)
	{
		TokenInfoReq ti = new TokenInfoReq();
		
		Claims claims = getAllClaimsFromToken(jwtToken);
		
		ti.setClient_id(claims.getSubject());
		ti.setClient_secret((String)claims.get("secretKey"));
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

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody(); 
	}	
	
	public static void main(String [] args)
	{
		JWTServiceImpl jsi = new JWTServiceImpl();
		OauthClients oauthDetails = new OauthClients();
		UserDetails userDetails = new OauthClients();
		oauthDetails.setClient_id("OpenCartAPISumit");
		oauthDetails.setClient_secret("$bARGbbF!l6CKdU0oaZEfo6UGv7B5Em6dS1SEN#0D!VI#adNQC");
		
		userDetails = oauthDetails;
		
		System.out.println(userDetails);
		
		String jwtToken = jsi.generateToken(userDetails);
		
		System.out.println(jwtToken);
		
		Claims claims = jsi.getAllClaimsFromToken(jwtToken);
		
		System.out.println(claims.getSubject());
		System.out.println(claims.get("secretKey"));
		System.out.println(claims.getExpiration());
		System.out.println(claims.size());		
	}	
}