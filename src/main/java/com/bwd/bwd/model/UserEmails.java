package com.bwd.bwd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "rka_user_email_tbl")
@Entity
@Getter
@Setter
public class UserEmails 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bwd_email_id")	
	private Long bwd_email_id;
	
	@Column(name = "useraccountid")
	private Long useraccountid;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "primarymail")
	private int primarymail;
	
/*
	 `bwd_email_id`, `useraccountid`, `email`, `type_id`, `created`, `modified`, `primary`, `date_verified`, `archived`
 */
}
