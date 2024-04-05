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
@Table(name = "user_token")
@Getter
@Setter
public class UesrTokenAuth {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "usertokenid")
	    private int usertokenid;

	    @Column(name = "companyid")
		private int companyid;

	    @Column(name = "comptoken")
	    private int comptoken;
	    
	    @Column(name = "useraccountid")
	    private int useraccountid;
	    
		@Column(name = "date")
		public java.sql.Timestamp date;
	    
	    public UesrTokenAuth()
		{
			
		}

		public UesrTokenAuth(int usertokenid, int companyid, int comptoken, int useraccountid)
		{						
			this.usertokenid = usertokenid;
			this.companyid = companyid;
			this.comptoken = comptoken;
			this.useraccountid = useraccountid;			
		}
		
		public UesrTokenAuth createToken(int id, int companyid, int comptoken)
		{
			java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
			this.useraccountid = id;
			this.companyid = companyid;
			this.comptoken = comptoken;
			this.date = (java.sql.Timestamp)dt;
			return this;
		}
	    
}
