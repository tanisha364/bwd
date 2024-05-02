package com.bwd.bwd.service;

import com.bwd.bwd.request.EmailDetails;

public interface EmailService {
	void sendEmail(EmailDetails details, String generatedLink, String email);
}