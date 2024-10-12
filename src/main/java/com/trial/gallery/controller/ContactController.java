package com.trial.gallery.controller;



import com.trial.gallery.dto.ContactFormDto;
import com.trial.gallery.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/submit")
    public String submitContactForm(@RequestBody ContactFormDto contactFormDto) {
        contactService.submitContactForm(contactFormDto);
        return "Contact form submitted successfully!";
    }
}

