package com.bwd.bwd.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author 
 *
 */
@Component
public class ReadFileService {

	String pathName = "";
	String fileName = "";	
	String inputFile = "";
	
	List<String> lines;
	
	InputStream input;
	
	public ReadFileService()
	{
		this.pathName = "src/main/resources";
		this.fileName = "application.properties";
	}
	
	public ReadFileService(String fileName)
	{
		this.pathName = "src/main/resources";		
		this.fileName = fileName;		
	}

	public ReadFileService(String pathName,String fileName)
	{
		this.pathName = pathName;		
		this.fileName = fileName;		
	}
	
	public void readFile()
	{
		inputFile = pathName+"/"+fileName;		
		try {
		      this.input = new FileInputStream(inputFile);
		      this.lines = printStream(input);
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public List<String> printStream(InputStream in) throws IOException {
		BufferedReader is = new BufferedReader(new InputStreamReader(in));
		List<String> line = new ArrayList<String>();
		String ln;
		while ((ln = is.readLine()) != null) {
			line.add(ln);			
		}
		return line;
	}
	
	public String getPropertyValue(String key)
	{
		String value = "";
		int cnt=0;
		readFile();		
		for(String line : this.lines)
		{		    	  
			String[] parts = line.split("=");
			
			if(parts.length>=2)
			{
				String keyline = parts[0];
				String valueline = parts[1];

				if(keyline.equals(key))
				{
					value = valueline;
					break;
				}
			}
		}
		return value;
	}
	
	public static void main(String [] args)
	{
		ReadFileService rfs = new ReadFileService();
		String key = "spring.datasource.url";
		String value = rfs.getPropertyValue(key);
		System.out.println(key +"  = "+value);
	}
}