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

@Entity
@Table(name = "registration_activity_tbl")
@Getter
@Setter
public class RegistrationActivity {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int registration_activitiy_id;

	    private int useraccountid;
	    private int companyid;
	    private int comptokenid;
	    private int jobid;
	    private int registrartion_type =1;
	    private String user_agent;
	    private String ip;
	    
		public java.sql.Timestamp date_added;
	    
	    public RegistrationActivity()
		{
			
		}

		
		public RegistrationActivity registrationActivity(int useraccountid, int companyid, int comptokenid, int jobid,String user_agent,String ip )
		{
			java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
			this.useraccountid = useraccountid;
			this.companyid = companyid;
			this.comptokenid = comptokenid;
			this.jobid = jobid;
			this.user_agent = user_agent;
			this.ip = ip;
			this.date_added = (java.sql.Timestamp)dt;
			return this;
		}
		
}
