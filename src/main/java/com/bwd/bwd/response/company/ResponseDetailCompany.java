package com.bwd.bwd.response.company;

import com.bwd.bwd.response.StatusResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDetailCompany {
	private StatusResponse status;
	private DetailsCompanyResponse data;
}
