package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class InviteResponse {

	private StatusResponse status;
	private Invite data;
	
}

