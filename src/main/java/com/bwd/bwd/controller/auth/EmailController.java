package com.bwd.bwd.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.request.EmailDetails;
import com.bwd.bwd.response.customEmailResponse;
import com.bwd.bwd.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendMail")
	public ResponseEntity<?> sendMail(@RequestBody EmailDetails details)
	{
		emailService.sendEmail(details);
		return ResponseEntity.ok(customEmailResponse.builder().message("Email sent....").status(HttpStatus.OK).success(true).build());   
	}
}