package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bwd.bwd.model.jobsmith.JobSearchResult;
import com.bwd.bwd.model.jobsmith.Reports;
import com.bwd.bwd.response.JobTitleResponse;

public class DBSearch {
	
	DBConnection dbc = null;
	
	Connection con;
	Statement stmt;
	ResultSet rs;

	public DBSearch()
	{
		dbc = new DBConnection();
		con = dbc.getConnection();
	}
	
	public List<Reports> findByCategoryType(int categoryId)
	{
		DBOperation dbop = new DBOperation();
		List<Reports> objects = null;
		String query = "SELECT * "
				+ " FROM reports_tbl rept"
				+ " INNER JOIN jobsmith_premade_reports_tbl jpre"
				+ " ON rept.rep_report_id = jpre.jobsmith_premade_reportid AND jpre.jobsmith_categoryid= "+categoryId+" AND jpre.archived = 0";
		
		dbop.setSelectQuery(query);
		dbop.executeSelectQuery();
		String data[][] = dbop.fetchRecord();	
		
		objects  = new ArrayList<Reports>();
		
		for(int i=0;i<=dbop.getNumberOfRow();i++)
		{
			Reports obj = new Reports();
			
			obj = obj.getObject(data[i]);
			
			System.out.println(obj);
			
			objects.add(obj);						
		}
		
		return objects;
	}
	
	public List<JobSearchResult> getObjects(String findValue)
	{
		DBOperation dbop = new DBOperation();
		String searchQuery = """
				Select 	DISTINCT(jobsmith_report_name)				   
				from 
				  jobsmith_report_tbl 
				WHERE 
				  jobsmith_report_name like ? 
				ORDER BY 
				  jobsmith_report_name
			""";
		
		dbop.preparedSelectQuery(searchQuery);
		dbop.executeSelectQuery(findValue);
		String data[][] = dbop.fetchRecord();	
		
		List <JobSearchResult> objects  = new ArrayList<JobSearchResult>();
		
		for(int i=0;i<=dbop.getNumberOfRow();i++)
		{
			JobSearchResult obj = new JobSearchResult();
			
			obj = obj.getObject(data[i]);
			
			System.out.println(obj);
			
			objects.add(obj);						
		}
		
		return objects;
	}
	
	public String getUserAccountId(String query, String field)
	{
		String uid = "000";	
		
		DBOperation dbop = new DBOperation();
		
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ "+query+"\n"+field);
		
		uid = dbop.executeQuery(query, field);		
		
		return uid;
	}
	
	public List<JobTitleResponse> getObjects(String findValue, int companyid)
	{
		DBOperation dbop = new DBOperation();
		DBOperation dbopLike = new DBOperation();
		
		String searchQuery = """
				Select 	jrt.jobsmith_reportid, jrt.jobsmith_report_name, jrt.useraccountid, CONCAT(ua.firstname, " ", ua.lastname) as username, jrt.date_modifed				   
				from 
				  jobsmith_report_tbl jrt
				  INNER JOIN user_accounts ua 
				  ON jrt.useraccountid = ua.useraccountid
				WHERE 
				  jobsmith_report_name = '"""+findValue+"' AND companyid = "+companyid+" AND archived = 0 "+"""
				ORDER BY 
				  jobsmith_report_name
			""";
		
		System.out.println(searchQuery);
		dbop.setSelectQuery(searchQuery);
		dbop.executeSelectQuery();
		String data[][] = dbop.fetchRecord();	
		
		List <JobTitleResponse> objects  = new ArrayList<JobTitleResponse>();

		String searchQueryLike = """
				Select 	jrt.jobsmith_reportid, jrt.jobsmith_report_name, jrt.useraccountid, CONCAT(ua.firstname, " ", ua.lastname) as username, jrt.date_modifed				   
				from 
				  jobsmith_report_tbl jrt
				  INNER JOIN user_accounts ua 
				  ON jrt.useraccountid = ua.useraccountid
				WHERE 
				  jobsmith_report_name like '%"""+findValue+"%' AND jobsmith_report_name != '"+findValue+"' AND companyid = "+companyid+" AND archived = 0 "+"""
				ORDER BY 
				  jobsmith_report_name
			""";
		System.out.println(searchQueryLike);
		dbopLike.setSelectQuery(searchQueryLike);
		dbopLike.executeSelectQuery();
		String data1[][] = dbopLike.fetchRecord();	
		
		for(int i=0;i<=dbop.getNumberOfRow();i++)
		{
			JobTitleResponse obj = new JobTitleResponse();
			
			obj = obj.getObject(data[i]);
			
			System.out.println(obj);
			
			objects.add(obj);						
		}
		
		System.out.println("NO : "+dbop.getNumberOfRow());
		
		if(dbop.getNumberOfRow()>=0)
		{

			for(int i=0;i<=dbopLike.getNumberOfRow();i++)
			{
				JobTitleResponse obj = new JobTitleResponse();

				obj = obj.getObject(data1[i]);

				System.out.println(obj);

				objects.add(obj);						
			}
 		}
		else
		{
			objects = null;
		}
		return objects;
	}		
	
	public static void main(String [] args)
	{
		DBSearch dbs = new DBSearch();
	//	dbs.getObjects("HR");
		dbs.getObjects("Scrum Master",819);		
	}
}