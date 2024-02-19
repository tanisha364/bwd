package com.bwd.bwd.model.auth;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Table(name = "user_accounts")
@Entity
@Getter
@Setter
public class UserAccountsAuth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "useraccountid")	
	private Long useraccountid;
	
	@Column(name = "regnum")	
	private String regnum;
	
	@Column(name = "username")	
	private String username;
	
	@Column(name = "email")	
	private String email;
	
	
	@Column(name = "userid")
	private String userid;	 
	
	@Column(name = "userlevel")	
	private int userlevel;
	
	@Column(name = "firstname")	
	private String firstname;
	
	@Column(name = "lastname")	
	private String lastname;
	
	//@Column(name = "hashpassword")	
	//private String hashpassword;	
	
	@Column(name = "status")
	private int status;
	
	@Column(name = "statusdate")
	private Date statusdate;
	
	@Column(name = "password")	
	private String password;	
	
	/*
	`user_accounts`
    `useraccountid`, `inviteeid`, `old_inviteeid`, `regnum`, `username`, `password`, `userid`, `userlevel`,
    `email`, `timestamp`, `firstname`, `lastname`, `status`, `statusdate`, `violation`, `status_approved`, `
     modified`, `lastvisit`, `linkid`, `option`, `customerid`, `landingassessmentid`, `isvet`	 
*/	

}
