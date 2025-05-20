package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendStatusNotification(String to, String jobTitle, String status) {
        String subject = "Statut de votre candidature";
        String message = "Bonjour,\n\nVotre candidature pour le poste de '" + jobTitle + "' a été " +
                (status.equals("APPROVED") ? "acceptée" : "refusée") + ".\n\nMerci pour votre intérêt.\n\nCordialement,\nJobHub";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
