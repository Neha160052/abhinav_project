package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.entity.user.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Async
public class EmailServiceImpl {

    JavaMailSender javaMailSender;

    public void sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);

            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("Error sending mail");
        }
    }

    public void sendActivationEmail(String firstName, String toMail, String token) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(toMail);
            mail.setSubject("Activate your account");
            mail.setText("Hi "+firstName+",\nPlease activate your account by clicking the link below:\n\nhttp://localhost:8080/api/customer/activate?token="+token);

            javaMailSender.send(mail);

            log.info("Sent email to email: "+toMail);
        } catch (Exception e) {
            log.error("Error sending mail");
        }
    }

    public void sendPasswordResetEmail(String firstName, String toMail, String token) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(toMail);
            mail.setSubject("Password Reset Request");
            mail.setText("Hi "+firstName+",\nPlease reset your password by clicking the link below:\n\nhttp://localhost:8080/api/auth/forgot-password/reset?token="+token);

            javaMailSender.send(mail);

            log.info("Sent email to email: "+toMail);
        } catch (Exception e) {
            log.error("Error sending mail");
        }
    }

    public void sendAdminActivationMail(User user) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(user.getEmail());
            mail.setSubject("Account activation successful");
            mail.setText("Hi "+user.getFirstName()+",\nYour account has been successfully activated by the Admin.");

            javaMailSender.send(mail);

            log.info("Sent email to email: "+user.getEmail());
        } catch (Exception e) {
            log.error("Error sending mail");
        }
    }

    public void sendAdminDeactivationMail(User user) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(user.getEmail());
            mail.setSubject("Account deactivated");
            mail.setText("Hi "+user.getFirstName()+",\nYour account has been deactivated by the Admin.");

            javaMailSender.send(mail);

            log.info("Sent email to email: "+user.getEmail());
        } catch (Exception e) {
            log.error("Error sending mail");
        }
    }
}
