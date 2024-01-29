package com.bwd.bwd.response;

import java.beans.JavaBean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@JavaBean
@Setter
@Getter
public class SavedJobReportsExpansionResponse {
	private List<SavedJobReportsExpansion> savedJobReportsExpansion;
}