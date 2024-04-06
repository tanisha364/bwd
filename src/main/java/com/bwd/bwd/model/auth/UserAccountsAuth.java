package com.bwd.bwd.model.auth;

import java.math.BigDecimal;
import java.sql.Date;

import com.bwd.bwd.util.DateTimeCreation;

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
			
	@Column(name = "userid")
	private String userid;	 
	
	@Column(name = "userlevel")	
	private Byte userlevel=-10;
	
	@Column(name = "firstname")	
	private String firstname;
	
	@Column(name = "lastname")	
	private String lastname;
	
	@Column(name = "linkid")	
	private String linkid="-1";
	
	@Column(name = "status")
	private BigDecimal status = BigDecimal.valueOf(1);
	
	public java.sql.Timestamp statusdate;
	
	@Column(name = "password")	
	private String password;	
	
	@Column(name = "refreshtoken")
	private String refreshtoken;	
	
	@Column(name = "isvet")
	private int isvet;
	
	@Column(name = "`option`")
	private int option;
	
	public UserAccountsAuth()
	{
		
	}
	
	public UserAccountsAuth createAccount(AccountRequest dataJRR, String regnum, String linkid, int option )
	{							
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
        this.firstname = dataJRR.getFirstname();
        this.lastname = dataJRR.getLastname();
        this.password = dataJRR.getPassword();
        this.isvet = dataJRR.getIsvet();
        this.statusdate = (java.sql.Timestamp)dt;
        
        System.out.println(":::::::::::::::::::::::: "+statusdate);
        this.regnum = regnum;
        this.linkid = linkid;
        this.option = option;
		return this;
	}	
}
