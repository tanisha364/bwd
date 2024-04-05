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

@Table(name = "user_association")
@Entity
@Getter
@Setter
public class UserAssociationAuth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userassociateid")
	private int userassociateid;

	@Column(name = "companyid")
	private int companyid;

	@Column(name = "tokenid")
	private int tokenid;

	@Column(name = "useraccountid")
	private int useraccountid;

	@Column(name = "accesslevel")
	private int accesslevel=10;

	@Column(name = "jobid")
	private int jobid;
	
	@Column(name = "date")
	public java.sql.Timestamp date;

	public UserAssociationAuth()
	{

	}

	public UserAssociationAuth(int userassociateid, int companyid, int tokenid, int useraccountid , int jobid)
	{		
		this.userassociateid = userassociateid;
		this.companyid = companyid;
		this.tokenid = tokenid;
		this.useraccountid = useraccountid;
		this.jobid = jobid;		
	}

	public UserAssociationAuth createAssociation(int id, int tokenid, int companyid , int jobid)
	{
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
		this.useraccountid = id;
		this.companyid = companyid;
		this.tokenid = tokenid;
		this.jobid = jobid;
		this.date = (java.sql.Timestamp)dt;
		return this;
	}
}
