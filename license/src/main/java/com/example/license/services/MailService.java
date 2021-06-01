package com.example.license.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String email, String resetPassLink) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("starterup.com@gmail.com", "StarterUp Project");
        helper.setTo(email);
        String subject = "Here is the link to reset your password";
        String content = "<p> Hello, </p>"
                + "<p>If you have requested to reset your password, click the link below to change it:</p>"
                + "<p><b><a href=\"" + resetPassLink + "\"> Reset my password </a></b></p>"
                + "<p>Ignore this email if you did not request to reset you password!</p> ";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}
