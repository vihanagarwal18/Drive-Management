package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Service.Interface.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendPasswordEmail(String to, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your Password for Drive Management");
            message.setText("Your password is: " + password);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to: " + to, e);
            throw e;
        }
    }
}