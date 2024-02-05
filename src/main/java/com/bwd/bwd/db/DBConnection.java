package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import org.springframework.stereotype.Service;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DBConnection 
{
	@PersistenceContext
	EntityManager entityManager;
	private String uriName;
	private String dsnName;
	private String driverName;
	
	private String username;
	private String password;
	
	Connection con;
	
	public void connectDB()
	{
		dsnName="devlive_working";
		uriName = "jdbc:mysql://192.168.200.67:3306/"+dsnName+"?createDatabaseIfNotExist=true";
		driverName = "com.mysql.cj.jdbc.Driver";
		username = "itneer";
		password = "Rdev@2022";

		try
		{
			if(con==null)
			{
				Class.forName(driverName);
				con=DriverManager.getConnection(uriName,username,password);
			}
		}catch(SQLException se){
			se.printStackTrace(System.err);
		}catch(ClassNotFoundException cnfe){
				cnfe.printStackTrace(System.err);
		}
	}	
	
	public Connection getConnection()
	{
		connectDB();
		return con;
	}
	
	public void disconnectDB()
	{
		try
		{
			if(con!=null)
			{
				con.close();
			}
		}catch(SQLException se){
			se.printStackTrace(System.err);
		}
	}
	
	public static void main(String [] args)
	{
		DBConnection dbc = new DBConnection();
		dbc.connectDB();
	}
}