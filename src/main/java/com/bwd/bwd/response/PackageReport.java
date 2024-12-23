package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class PackageReport {
	
	private int comptokenid;
	private int ProductId;
	private String PackageName;
	private List<Report> Reports;
}
