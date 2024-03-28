package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class PermissionResponse {

	private int isAccess;
	public ArrayList<actionPermi> action;
	
	public PermissionResponse() {
		action = new ArrayList<>();
	}

	public PermissionResponse getObject(String data[]) 
	{
		this.isAccess = Integer.parseInt(data[0]);
		return this;
	}
}
