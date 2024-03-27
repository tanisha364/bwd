package com.bwd.bwd.util;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.time.LocalDateTime; 
import java.sql.Date;

public class DateTimeCreation {

	public static String getCurrentDatetime()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		return dtf.format(now);
	}
	
	public static Date getSqlDate()
	{
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		long millis=System.currentTimeMillis();  
	      
	    // creating a new object of the class Date  
	    java.sql.Date date = new java.sql.Date(millis);       
	    System.out.println(date);
	    return date;
	}
	
	public static java.sql.Timestamp getSqlTimestamp()
	{
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		
		return ourJavaTimestampObject;
	}
				
    
    
	public static void main(String args[])
	{
		System.out.println(DateTimeCreation.getCurrentDatetime());
	}
}
