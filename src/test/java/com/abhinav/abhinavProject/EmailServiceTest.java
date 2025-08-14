package com.abhinav.abhinavProject;

import com.abhinav.abhinavProject.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail_shouldSendEmail() {
        emailService.sendMail("abhinav.chaudhary@tothenew.com", "Email service Test", "Hello. Message aya kya?");
    }
}
