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

@Table(name = "invitation_tbl")
@Entity
@Getter
@Setter
public class UserInvite {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invitation_id;

	private int companyid;
    
    private int landing_assessment_id;
    
    private int inviteed_by;

    private int invitee_email_type;
    
    private int is_admin;

    private String invitee_firstname;
    
    private String invitee_lastname;
    private String invitee_email;
    private String invitee_tel;
    
    private int invitee_tel_code;
    private int participant_type;
    private int comptokenid;
    private int jobid;
    
    private String invitation_code;

	public java.sql.Timestamp date_inivted;
    
    
	public UserInvite()
	{
		
	}

	/*
	 * public UserInvite(long bwdEmailId, long useraccountid,String email) {
	 * this.bwdEmailId = bwdEmailId; this.useraccountid = useraccountid; this.email
	 * = email; }
	 */
	
	public UserInvite create(int companyid,int landing_assessment_id,int inviteed_by,int invitee_email_type,String invitee_firstname,String invitee_lastname,String invitee_email,String invitee_tel,int invitee_tel_code,int participant_type,int comptokenid,int jobid,String invitation_code, int is_admin)
	{		
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();		
		this.companyid = companyid;
		this.landing_assessment_id = landing_assessment_id;
		this.inviteed_by = inviteed_by;
		
		this.invitee_email_type = invitee_email_type;
		this.invitee_firstname = invitee_firstname;
		this.invitee_lastname = invitee_lastname;
		this.invitee_email = invitee_email;
		this.invitee_tel = invitee_tel;
		this.invitee_tel_code = invitee_tel_code;
		this.participant_type = participant_type;
		this.comptokenid = comptokenid;
		this.jobid = jobid;
		this.invitation_code = invitation_code;
		this.is_admin = is_admin;
		
		this.date_inivted = (java.sql.Timestamp)dt;	
		return this;
	}    
}
