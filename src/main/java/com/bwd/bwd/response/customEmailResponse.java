package com.bwd.bwd.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class customEmailResponse {
	 
	  private String message;
	  private HttpStatus status;
	  private boolean success = false;
}
