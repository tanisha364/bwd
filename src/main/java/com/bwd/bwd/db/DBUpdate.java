package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bwd.bwd.model.jobsmith.JobsmithReport;
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
	        
	        // Use a prepared statement to avoid SQL injection
	        String sqlUpdate = "UPDATE jobsmith_report_tbl SET archived = " + newArchive+" WHERE jobsmith_reportid = "+ jobsmithReportId;
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
	        
	        // Use a prepared statement to avoid SQL injection
	        String sqlUpdate = "UPDATE jobsmith_report_tbl SET report_status = '" + reportStatus+"', status_date=current_timestamp  WHERE jobsmith_reportid = "+ jobsmithReportId;
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
	        System.out.println(jobsmithReportId);
	        
	        // Use a prepared statement to avoid SQL injection
	        String sqlUpdate = "UPDATE jobsmith_report_tbl SET locked = " + newLocked+" WHERE jobsmith_reportid = "+ jobsmithReportId;
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