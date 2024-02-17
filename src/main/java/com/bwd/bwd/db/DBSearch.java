package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bwd.bwd.model.jobsmith.JobSearchResult;
import com.bwd.bwd.model.jobsmith.Reports;

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
	
	public static void main(String [] args)
	{
		DBSearch dbs = new DBSearch();
		dbs.getObjects("HR");
	}
}