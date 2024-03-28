package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean

public class actionPermi {

	public String name;
	
	public actionPermi() {

	}

	public actionPermi getObject(String arr[]) {
		this.name = arr[0];

		return this;
	}
}
