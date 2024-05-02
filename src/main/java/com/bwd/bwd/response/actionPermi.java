package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean

public class actionPermi {

	public String id;
	
	public actionPermi() {

	}

	public actionPermi getObject(String arr[]) {
		this.id = arr[0];

		return this;
	}
}
