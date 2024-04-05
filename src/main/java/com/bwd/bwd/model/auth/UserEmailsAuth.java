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
    private Long bwdEmailId;

    @Column(name = "useraccountid")
	private Long useraccountid;

    @Column(name = "email")
    private String email;
    
    @Column(name = "created")
	public java.sql.Timestamp created;
    
    public java.sql.Timestamp date_verified;
    
    @Column(name = "`primary`")
    private int primary = 1;
    
	public UserEmailsAuth()
	{
		
	}

	public UserEmailsAuth(long bwdEmailId, long useraccountid,String email)
	{			
		this.bwdEmailId = bwdEmailId;
		this.useraccountid = useraccountid;
		this.email = email;		
	}
	
	public UserEmailsAuth createEmail(long id,String email)
	{		
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();		
		this.useraccountid = id;
		this.email = email;
		this.created = (java.sql.Timestamp)dt;		
		this.date_verified = (java.sql.Timestamp)dt;	
		return this;
	}    
}
