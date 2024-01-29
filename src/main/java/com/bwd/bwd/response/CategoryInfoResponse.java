package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Getter
@Setter
public class CategoryInfoResponse {
	private StatusResponse status;
	private CategoryResponse data;
}
