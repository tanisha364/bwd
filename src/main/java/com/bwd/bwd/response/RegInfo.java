package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class RegInfo {
	private String linkid;
	
	public RegInfo()
	{
		
	}
	
	public RegInfo(String linkid) {
		super();
		this.linkid = linkid;
	}
	
	public RegInfo getObject(String arr[])
	{
		this.linkid = arr[0];		
		return this;
	}
}
