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
@Table(name = "password_history_tbl")
@Getter
@Setter
public class PasswordHistory {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "password_history_id")
	    private int password_history_id;
	    
	    @Column(name = "useraccountid")
	    private long useraccountid;
	     	    
	    @Column(name = "password")	
	    private String password;
	    
		@Column(name = "date_set")
		public java.sql.Timestamp date_set;
		 
		 public PasswordHistory createPasswordHistory(long useraccountid, String password)
			{
				java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
				this.useraccountid = useraccountid;
				this.password =password; 
				this.date_set = (java.sql.Timestamp)dt;
				return this;
			}
}
