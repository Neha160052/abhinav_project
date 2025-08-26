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
            log.info("Sent email to: " + to);
        } catch (Exception e) {
            log.error("Error sending mail");
        }
    }

    public void sendActivationEmail(String firstName, String toMail, String token) {
        sendMail(
                toMail,
                "Activate your account",
                "Hi " + firstName + ",\nPlease activate your account by clicking the link below:\n\nhttp://localhost:8080/api/customer/activate?token=" + token
        );
    }

    public void sendPasswordResetEmail(String firstName, String toMail, String token) {
        sendMail(
                toMail,
                "Password Reset Request",
                "Hi "+firstName+",\nPlease reset your password by clicking the link below:\n\nhttp://localhost:8080/api/auth/forgot-password/reset?token=" + token
        );
    }

    public void sendAdminActivationMail(User user) {
        sendMail(
                user.getEmail(),
                "Account activation successful",
                "Hi "+user.getFirstName()+",\nYour account has been successfully activated by the Admin."
        );
    }

    public void sendAdminDeactivationMail(User user) {
        sendMail(
                user.getEmail(),
                "Account deactivated",
                "Hi " + user.getFirstName() + ",\nYour account has been deactivated by the Admin."
        );
    }

    public void sendPasswordUpdateMail(User user) {
        sendMail(
                user.getEmail(),
                "Password updated successfully.",
                "Hi " + user.getFirstName() + ",\nYour account password has been updated successfully."
        );
    }
}
