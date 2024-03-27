package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bwd.bwd.request.JobsmithReportRequest;
import com.bwd.bwd.request.JobsmithReportRequestEdit;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.StatusResponse;

public class DBUpdate {

DBConnection dbc = null;
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	public DBUpdate()
	{
		dbc = new DBConnection();
		con = dbc.getConnection();		
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	/*
	 * public void updateObject(JobsmithReport JobsmithReport) { DBOperation dbop =
	 * new DBOperation();
	 * 
	 * String sqlUpdate =
	 * "UPDATE jobsmith_report_tbl SET archived = 1 WHERE jobsmith_reportid = 5";
	 * dbop.updateRecord(sqlUpdate); }
	 */
	
	 public StatusResponse updateObject(UserData data) {
		    StatusResponse sr = new StatusResponse();
		    
	        DBOperation dbop = new DBOperation();

	        int jobsmithReportId = data.getReportId();
	        int newArchive =  data.getArchived();
	        System.out.println(jobsmithReportId);
	        int companyid = data.getCompanyid();
	        System.out.println(jobsmithReportId);
	        
	        String accountIdQuery = "SELECT ua.useraccountid as uid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = '"+data.getUserid()+"'";
		
	        DBSearch dbs = new DBSearch();
			String accountIdData = dbs.getUserAccountId(accountIdQuery,"uid");
	        Long profileAccountId =  Long.parseLong(accountIdData); 
	        
	        // Use a prepared statement to avoid SQL injection
	        String sqlUpdate = "UPDATE jobsmith_report_tbl a "
	        		+ "INNER JOIN user_accounts b ON a.useraccountid = b.useraccountid "
	        		+ "INNER JOIN jobsmith_user_profile_tbl c ON b.useraccountid = c.useraccountid SET archived = " + newArchive+" WHERE a.jobsmith_reportid = "+ jobsmithReportId+" AND c.companyid ="+companyid+" AND c.isAccess = 1";
	        System.out.println(sqlUpdate);
	        dbop.updateRecord(sqlUpdate);
	        
	        sr.setStatusCode(dbop.getStatusCode());
	        
	        if(dbop.getStatusCode() == 1)
	        {
	        	if(dbop.getCountUpdated() > 0)
	        	{
	        		sr.setValid(true);
	        		sr.setMessage("No. of records Updated : "+dbop.getCountUpdated());
	        	}
	        	else
	        	{
	        		sr.setValid(false);
	        		sr.setMessage("No record Updated");
	        	}
	        }
	        
	        return sr;
	    }
	 
	 public StatusResponse udpatereportstatus(UserData data) {
		    StatusResponse sr = new StatusResponse();
		    
	        DBOperation dbop = new DBOperation();

	        int jobsmithReportId = data.getReportId();
	        String reportStatus =  data.getReport_status();
	        System.out.println(jobsmithReportId);
	        int companyid = data.getCompanyid();
	        System.out.println(jobsmithReportId);
	        
	        String accountIdQuery = "SELECT ua.useraccountid as uid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = '"+data.getUserid()+"'";
		
	        DBSearch dbs = new DBSearch();
			String accountIdData = dbs.getUserAccountId(accountIdQuery,"uid");
	        Long profileAccountId =  Long.parseLong(accountIdData); 
	        
	        // Use a prepared statement to avoid SQL injection
	        String sqlUpdate =  "UPDATE jobsmith_report_tbl a "
	        		+ "INNER JOIN user_accounts b ON a.useraccountid = b.useraccountid "
	        		+ "INNER JOIN jobsmith_user_profile_tbl c ON b.useraccountid = c.useraccountid SET report_status = '" + reportStatus+"', status_date=current_timestamp  WHERE a.jobsmith_reportid = "+ jobsmithReportId+" AND c.companyid ="+companyid+" AND c.isAccess = 1";
	        System.out.println(sqlUpdate);
	        dbop.updateRecord(sqlUpdate);
	        
	        sr.setStatusCode(dbop.getStatusCode());
	        
	        if(dbop.getStatusCode() == 1)
	        {
	        	if(dbop.getCountUpdated() > 0)
	        	{
	        		sr.setValid(true);
	        		sr.setMessage("No. of records Updated : "+dbop.getCountUpdated());
	        	}
	        	else
	        	{
	        		sr.setValid(false);
	        		sr.setMessage("No record Updated");
	        	}
	        }	        
	        return sr;
	    }
	 
	 public StatusResponse updateRecord(JobsmithReportRequest data)
	 {
		 DBOperation dbop = new DBOperation();
		 StatusResponse sr = new StatusResponse();
		 String sqlUpdate = """
						UPDATE 
						  `jobsmith_report_tbl` 
						SET 
						  `jobsmith_report_name` = '"""+data.getJobsmith_report_name()+""" 
						  ',`jobsmith_report_note` = '"""+data.getJobsmith_report_note()+"""
						  ',`companyid` = """+data.getCompanyid()+"""						  		 
						  ,`useraccountid` = """+data.getUseraccountid()+""" 
						  ,`date_modifed` = current_timestamp 
						  ,`status_date` = current_timestamp
						WHERE 
						  `jobsmith_reportid` = """+data.getJobsmith_reportid(); 
		 
		 System.out.println(sqlUpdate);

		 dbop.updateRecord(sqlUpdate);

		 sr.setStatusCode(dbop.getStatusCode());

		 if(dbop.getStatusCode() == 1)
		 {
			 if(dbop.getCountUpdated() > 0)
			 {
				 sr.setValid(true);
				 sr.setMessage("No of records Updated : "+dbop.getCountUpdated());
			 }
			 else
			 {
				 sr.setValid(false);
				 sr.setMessage("No record Updated");
			 }
		 }

		 return sr;		 
	 }	 
	 
	 public StatusResponse updateLock(UserData data) {
		    StatusResponse sr = new StatusResponse();
		    
	        DBOperation dbop = new DBOperation();

	        int jobsmithReportId = data.getReportId();
	        int newLocked =  data.getLocked();
	        int companyid = data.getCompanyid();
	        System.out.println(jobsmithReportId);
	        
	        String accountIdQuery = "SELECT ua.useraccountid as uid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = '"+data.getUserid()+"'";
		
	        DBSearch dbs = new DBSearch();
			String accountIdData = dbs.getUserAccountId(accountIdQuery,"uid");
	        Long profileAccountId =  Long.parseLong(accountIdData); 

	        
	        // Use a prepared statement to avoid SQL injection
	        String sqlUpdate = "UPDATE jobsmith_report_tbl a "
	        		+ "INNER JOIN user_accounts b ON a.useraccountid = b.useraccountid " 
	        		+ "INNER JOIN jobsmith_user_profile_tbl c ON b.useraccountid = c.useraccountid SET a.locked = " + newLocked+" WHERE a.jobsmith_reportid = "+ jobsmithReportId+" AND c.companyid ="+companyid+" AND c.isAccess = 1";
	        System.out.println(sqlUpdate);
	        dbop.updateRecord(sqlUpdate);
	        
	        sr.setStatusCode(dbop.getStatusCode());
	        
	        if(dbop.getStatusCode() == 1)
	        {
	        	if(dbop.getCountUpdated() > 0)
	        	{
	        		sr.setValid(true);
	        		sr.setMessage("No. of records Updated : "+dbop.getCountUpdated());
	        	}
	        	else
	        	{
	        		sr.setValid(false);
	        		sr.setMessage("No record Updated");
	        	}
	        }
	        
	        return sr;
	    }
	 
	 public StatusResponse updateRecord(JobsmithReportRequestEdit data,boolean forDelete)
	 {
		    StatusResponse sr = new StatusResponse();
		    
	        DBOperation dbop = new DBOperation();
       
        	String sqlUpdate = "UPDATE jobsmith_report_capability_tbl SET weightage = '" + data.getWeightage()+"' , sequence = "+data.getSequence()+" WHERE jobsmith_report_capabilityid = "+ data.getJobsmith_report_capabilityid();
	        if(forDelete == true)
	        {
	        	sqlUpdate = "UPDATE jobsmith_report_capability_tbl SET archived = -9 WHERE jobsmith_report_capabilityid = "+ data.getJobsmith_report_capabilityid();
	        }
	        System.out.println(sqlUpdate);
	        dbop.updateRecord(sqlUpdate);
	        
	        sr.setStatusCode(dbop.getStatusCode());
	        
	        if(dbop.getStatusCode() == 1)
	        {
	        	if(dbop.getCountUpdated() > 0)
	        	{
	        		sr.setValid(true);	        		
	        		sr.setMessage("No of records Updated : "+dbop.getCountUpdated());
	        		if(forDelete == true)
	        		{
	        			sr.setMessage("No of records Deleted : "+dbop.getCountUpdated());
	        		}
	        	}
	        	else
	        	{
	        		sr.setValid(false);
	        		sr.setMessage("No record Updated");
	        		if(forDelete == true)
	        		{
	        			sr.setMessage("No record Deleted : "+dbop.getCountUpdated());
	        		}
	        	}
	        }
	        
	        return sr;
	 }	 
	 
}