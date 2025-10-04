package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Service.Interface.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendPasswordEmail(String to, String password, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Your Password for Drive Management");

            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<div style='margin: auto; max-width: 600px; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                    + "<div style='text-align: center;'>"
                    + "<img src='cid:logo' alt='Drive Management Logo' style='width: 150px; height: auto;'/>"
                    + "</div>"
                    + "<h2 style='color: #007bff;'>Your Password for Drive Management</h2>"
                    + "<p>Hello, <strong>" + username + "</strong>!</p>"
                    + "<p>Your password has been successfully retrieved. Please keep it safe and do not share it with anyone.</p>"
                    + "<p style='background-color: #f2f2f2; padding: 10px; border-radius: 5px;'>"
                    + "<strong>Password:</strong> " + password
                    + "</p>"
                    + "<p>If you did not request this, please contact our support team immediately.</p>"
                    + "<hr>"
                    + "<p style='font-size: 0.8em; color: #777; text-align: center;'>"
                    + "&copy; 2025 Drive Management. All rights reserved."
                    + "</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);

            ClassPathResource logo = new ClassPathResource("static/logo512.png");
            helper.addInline("logo", logo);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to: " + to, e);
            throw new RuntimeException(e);
        }
    }
}