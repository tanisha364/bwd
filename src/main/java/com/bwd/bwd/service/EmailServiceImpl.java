package com.bwd.bwd.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.display.name}")
    private String senderDisplayName;

    @Override
    public void sendEmail(String generatedLink, String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(sender, senderDisplayName);
            helper.setTo(email);
            helper.setSubject("Your BestWork DATA User Registration has been received");

            String emailText = "Welcome! You've just registered at BestWork DATA with the following information:"
            		+ "  You will need to verify your email address before you can login to your account.  This is not required to complete the questionnaire. "
            		+ "To verify your email address simply click the following link: (if you cannot click link, then copy and paste into your browser)\n" + generatedLink;
            helper.setText(emailText);

            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } 
    }
}