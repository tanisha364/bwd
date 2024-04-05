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
@Table(name = "user_tel_tbl")
@Getter
@Setter
public class UserTelsAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_tel_id")
    private Long userTelId;

    @Column(name = "useraccountid")
	private Long useraccountid;

    @Column(name = "tel")
    private String tel;

    @Column(name = "tel_code")
    private int telCode;
    
    @Column(name = "date_added")
   	public java.sql.Timestamp date_added;
    
    public UserTelsAuth()
	{
		
	}

	public UserTelsAuth(long userTelId, long useraccountid,String tel, int telCode)
	{	
		this.userTelId = userTelId;
		this.useraccountid = useraccountid;
		this.tel = tel;
		this.telCode = telCode;		
	}
	
	public UserTelsAuth createTel(long id,String tel, int telCode)
	{
		java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
		this.useraccountid = id;
		this.tel = tel;
		this.telCode = telCode;
		this.date_added = (java.sql.Timestamp)dt;
		return this;
	}
}

