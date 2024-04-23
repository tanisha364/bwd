package com.bwd.bwd.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bwd.bwd.request.EmailDetails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;
    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Override
	public void sendEmail(EmailDetails details) {
		MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

			//SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
        helper.setFrom(sender);
        helper.setTo(details.getTo());
        helper.setSubject(details.getSubject());
        helper.setText(details.getText());

        javaMailSender.send(message);
    } catch (MessagingException e) {
        // Handle exception
        e.printStackTrace();
    }
}
	
}
