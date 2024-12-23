package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class Participantinfo {

	String participant_type;
	int participant_type_id;
	
	public Participantinfo create(int participant_type_id, String participant_type)
	{
		this.participant_type_id = participant_type_id;
		this.participant_type = participant_type;
		return this;
	}
	
	public Participantinfo() {

	}
}
