package com.bwd.bwd.model.auth;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class AccountRequest {

	private String code;
	private int landingid;
	private int useraccountid;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String tel;
	private int telCode;
	private int isvet;
	private int participanttype;
	private String useragent;
	private String ip;
}
