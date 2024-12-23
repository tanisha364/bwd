package com.bwd.bwd.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteRequest {

	
	private int useraccountid;
	private String userid;
	
	private int is_admin;
	private int companyid;

	private int comptokenid;
	private int landingassessmentid;
	private int invitee_email_type;

    private String invitee_firstname;
    
    private String invitee_lastname;
    private String invitee_email;
    private String invitee_tel;
    
    private int invitee_tel_code;
    private int participant_type;
    private int jobid;
}
