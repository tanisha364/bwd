package com.bwd.bwd.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenResponse {
	private StatusResponse status;	
	private UserTokenInfo userToken;

}
