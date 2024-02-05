package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bwd.bwd.model.jobsmith.JobsmithReport;
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
	 
	 public StatusResponse udpatereportstatus(UserData data) {
		    StatusResponse sr = new StatusResponse();
		    
	        DBOperation dbop = new DBOperation();

	        int jobsmithReportId = data.getReportId();
	        String reportStatus =  data.getReport_status();
	        System.out.println(jobsmithReportId);
	        
	        // Use a prepared statement to avoid SQL injection
	        String sqlUpdate = "UPDATE jobsmith_report_tbl SET report_status = '" + reportStatus+"' WHERE jobsmith_reportid = "+ jobsmithReportId;
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
}