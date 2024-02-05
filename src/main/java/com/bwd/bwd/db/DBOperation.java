package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBOperation {
	
	DBConnection dbc;
	
	private String sqlSelect;	

	private int countColumn;
	
	private String tableName; 
	private int numberOfColumns;
	private int numberOfRow;
	private String [][] records;
	private String [] columnNames;
	private String columnName;	
	
	Connection con;
	Statement stmt;
	PreparedStatement pstmt;
	ResultSet rs;		
	
	int statusCode;	
	private int countUpdated;
	
	public DBOperation()
	{
		dbc = new DBConnection();
		con = dbc.getConnection();		
	}	
	
	public void createSelectQuery(String tableName)
	{
		sqlSelect = "Select * from "+tableName;
	}
	
	public void setSelectQuery(String sqlQuery)
	{	
		this.sqlSelect = sqlQuery;
	}
	public void executeSelectQuery()
	{		
		try
		{
			stmt=con.createStatement();
			rs=stmt.executeQuery(sqlSelect);
		}catch(SQLException se){
			se.printStackTrace(System.err);
		}
	}	

	public void preparedSelectQuery(String sqlSelect)
	{		
		try
		{
			pstmt=con.prepareStatement(sqlSelect);			
		}catch(SQLException se){
			se.printStackTrace(System.err);
		}
	}	

	public void executeSelectQuery(String findValue)
	{		
		try
		{
			System.out.println(findValue);
			pstmt.setString(1, "%"+ findValue +"%");			
			rs=pstmt.executeQuery();
		}catch(SQLException se){
			se.printStackTrace(System.err);
		}
	}		
	
	public String getTitle(String str)
	{
		String title;
		char ch;
		char ch1;
		int ln;
		
		StringBuilder sb = new StringBuilder(str);
		for(int i=0;i<sb.length();i++)
		{
			ch = sb.charAt(i);
			if(i!=0 && Character.isUpperCase(ch))
			{
				sb.insert(i," ");
				i++;
			}
		}
		
		if(sb.charAt(0)>=97 && sb.charAt(0)<=122)
		{
			ch1 = (char) (sb.charAt(0)-32);
			String ch2 = ""+ch1;
			sb.setCharAt(0,ch1);
		}
	
		System.out.println(sb);	
		return sb.toString();
	}	
	
	
	public int getNumberOfRow() {
		return numberOfRow;
	}

	public void setNumberOfRow(int numberOfRow) {
		this.numberOfRow = numberOfRow;
	}

	public String [][]  fetchRecord()
	{
		try
		{	
	    	ResultSetMetaData rsmd = rs.getMetaData();
    	 	numberOfColumns = rsmd.getColumnCount();
    	 	numberOfRow = 1200; //getRowCount(rs);
		
			columnNames = new String[100];    	 	

			tableName = getTitle(rsmd.getTableName(1));
			System.out.println("---------Table------"+tableName);
	  		
	  		int i=1;countColumn=rsmd.getColumnCount();
			
			while(i<=countColumn)
			{
	      		columnName = rsmd.getColumnName(i); //rsmd.getColumnLabel(i);
	      		columnName = getTitle(columnName);
	      		columnNames[i-1] = columnName;
	      		i++;		  		
			}
			
			records = new String[numberOfRow][numberOfColumns];
			int cntRow=-1;
			while(rs.next())
			{	
				cntRow++;
	    	 	for(int cntColumn=1;cntColumn<=numberOfColumns;cntColumn++)
    	 		{	
					records[cntRow][cntColumn-1] = rs.getString(cntColumn);
					System.out.println(records[cntRow][cntColumn-1]);
				}
			}
			numberOfRow = cntRow;
			rs.close();
			if(stmt != null)
			{
				stmt.close();
			}
		}catch(SQLException se){
			se.printStackTrace(System.err);
		}
		return records;
	}
	
	public String [][] getRecords(ArrayList<String> al,int numberOfRow,int numberOfColumns)
	{
		String [][] rec = new String[numberOfRow][numberOfColumns];
		int k=0;
		for(int i=0;i<numberOfRow;i++)
		{
			for(int j=0;j<numberOfColumns;j++)
			{	
				rec[i][j] = (String) al.get(k);
				k++;
			//	System.out.println("K - > "+k);
				
			}
		}		
		return rec;
	}
	
	public void insertRecord(String sqlInsert)
	{
		try
		{
			stmt=con.createStatement();
			int countInserted = stmt.executeUpdate(sqlInsert);
		} catch(SQLException ex) {
	         ex.printStackTrace();
	      }
		
	}
	
	public void updateRecord(String sqlUpdate)
	{
		try
		{
			stmt=con.createStatement();
			//			System.out.println(sqlUpdate);
			countUpdated = stmt.executeUpdate(sqlUpdate);
			statusCode = 1;
		} catch(SQLException ex) {
			//statusCode = 0;
			ex.printStackTrace();
		}
	}
	
	public void deleteRecord(String sqlDelete)
	{
		try
		{
			stmt=con.createStatement();
//			System.out.println(sqlDelete);
			int countInserted = stmt.executeUpdate(sqlDelete);
		} catch(SQLException ex) {
	         ex.printStackTrace();
	      }
		
	}	
	
	public int getCountUpdated() {
		return countUpdated;
	}

	public void setCountUpdated(int countUpdated) {
		this.countUpdated = countUpdated;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public static void main(String [] args)
	{
		DBOperation dbop = new DBOperation();
		
		String sqlInsert = """
				INSERT INTO `rka_user_accounts`(
					  `useraccountid`, `email`, `password`, 
					  `hashpassword`, `regnum`, `userlevel`, 
					  `username`, `firstname`, `lastname`
					) 
					VALUES 
					  (
					    77777,"rajesha@gmail.com","abc", "1234", 
					    "BWD-77777", 1, "ABC", 
					    "AAA", "BBBCCC"
					  )
				""";
		dbop.insertRecord(sqlInsert);
		
		dbop.createSelectQuery("rka_user_accounts where `useraccountid` = 77777 ");		
		dbop.executeSelectQuery();
		dbop.fetchRecord();	
		
//		String sqlUpdate = """
//					UPDATE 
//					  `rka_user_accounts` 
//					SET 					  
//					  `userlevel` = 3					 
//					WHERE 
//					  `useraccountid` = 77777 AND `userlevel` = 1 				
//				""";
//		dbop.updateRecord(sqlUpdate);
						
//		String sqlDelete = """
//					DELETE FROM 
//					  `rka_user_accounts` 
//					WHERE 
//					 `useraccountid` = 77777  
//				""";
//		dbop.updateRecord(sqlDelete);
	}
}
