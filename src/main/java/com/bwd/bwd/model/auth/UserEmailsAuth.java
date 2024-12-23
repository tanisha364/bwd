package com.bwd.bwd.model.auth;

import com.bwd.bwd.util.DateTimeCreation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user_email_tbl")
@Entity
@Getter
@Setter
public class UserEmailsAuth 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bwd_email_id")
    private int bwdEmailId;

    @Column(name = "useraccountid")
	private int useraccountid;

    @Column(name = "email")
    private String email;
    
    @Column(name = "type_id")
    private int type_id;
    
    @Column(name = "verificationid")
    private String verificationid;
    
    @Column(name = "created")
	public java.sql.Timestamp created;
    
    @Column(name = "date_verified")
	public java.sql.Timestamp date_verified;
    
    @Column(name = "`primary`")
    private int primary = 1;
    
	public UserEmailsAuth()
	{
		
	}

	public UserEmailsAuth(int bwdEmailId, int useraccountid,String email)
	{			
		this.bwdEmailId = bwdEmailId;
		this.useraccountid = useraccountid;
		this.email = email;		
	}
	
	public UserEmailsAuth createEmail(int id,String email, String verificationid)
	{		
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();		
		this.useraccountid = id;
		this.email = email;
		this.verificationid = verificationid;
		this.created = (java.sql.Timestamp)dt;	
		return this;
	}   
	
	public UserEmailsAuth createEmail1(int id,String email,int type_id, int primary)
	{		
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();		
		this.useraccountid = id;
		this.email = email;
		this.type_id =type_id;
		this.primary = primary;
		this.created = (java.sql.Timestamp)dt;
		this.date_verified = (java.sql.Timestamp)dt;
		return this;
	}    
}
