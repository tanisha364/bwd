package com.bwd.bwd.model.jobsmith;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name="capability_tbl")
@Entity
@Getter
@Setter
public class JobsmithCapabilities {
	
	@Id	
	@Column(name = "Cap_Capabilityid")	
	private int Cap_CapabilityiId;
	
//	@Column(name = "Cap_CapabilityId")	
//	private Long abc;
	
	@Column(name = "Cap_Capability")
	private String Cap_Capability;
	
	@Column(name= "Cap_Description")
	private String Cap_Description;
	
	private int categoryid;
	
	
}
