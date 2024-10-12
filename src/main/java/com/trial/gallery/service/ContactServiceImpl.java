package com.trial.gallery.service;

import com.trial.gallery.dto.ContactFormDto;
import com.trial.gallery.service.EmailService; // Import the email service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private EmailService emailService; // Autowire EmailService

    @Override
    public void submitContactForm(ContactFormDto contactFormDto) {
        String recipientEmail = "recipient_email@example.com"; // Replace with actual recipient email
        String subject = "New Contact Form Submission from " + contactFormDto.getName();
        String body = "Name: " + contactFormDto.getName() +
                "\nEmail: " + contactFormDto.getEmail() +
                "\nPhone: " + contactFormDto.getPhone() +
                "\nMessage: " + contactFormDto.getMessage();

        emailService.sendEmail(recipientEmail, subject, body);
    }
}
