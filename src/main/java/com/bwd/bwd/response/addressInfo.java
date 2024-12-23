package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class addressInfo {
	
	private billaddrInfo Billing;
	private mailaddrInfo Mailing;

	int Isusermailingsame;
	
}
