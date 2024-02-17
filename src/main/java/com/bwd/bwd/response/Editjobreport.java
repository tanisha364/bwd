package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class Editjobreport {

	 private String jobreportid;
	    private String jobtitle; 
	    private String jobnote;
	    private String companyid;
	    private String useraccountid;
	    private String reportstatus;
	    public ArrayList<Jobedit> critical;
		public ArrayList<Jobedit> important;
		public ArrayList<Jobedit> nicetohave;	
	    
	    public Editjobreport() {
	    	critical = new ArrayList<>();
	        important = new ArrayList<>();
	        nicetohave = new ArrayList<>();
	    }

	    public Editjobreport getObject(String arr[]) {
	        this.jobreportid = arr[0];
	        this.jobtitle = arr[1];
	        this.jobnote = arr[2];
	        this.companyid = arr[3];
	        this.useraccountid = arr[4];
	        this.reportstatus = arr[5];
	     
	        return this;
	    }
	}