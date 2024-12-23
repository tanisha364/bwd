package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class Invite {

	private List<PackageReport> Package;
	private List<Participantinfo> Participant;
	private List<Departmentinfo> Department;
}

