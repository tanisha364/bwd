package com.bwd.bwd.response;


import java.util.List;

import com.bwd.bwd.model.jobsmith.JobsmithCapabilities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CapabilityResponse {
	private List<JobsmithCapabilities> capbilities;
}
