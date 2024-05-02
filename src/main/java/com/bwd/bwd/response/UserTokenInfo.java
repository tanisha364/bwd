package com.bwd.bwd.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenInfo {
	private String userToken;	
	private String refreshToken; 	
}