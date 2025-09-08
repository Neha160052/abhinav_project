package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.config.AdminProps;
import com.abhinav.abhinavProject.entity.product.Product;
import com.abhinav.abhinavProject.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductNotificationServiceImpl {

    ProductRepository productRepository;
    EmailServiceImpl emailService;
    AdminProps adminProps;

    /**
     * Scheduled job to notify the admin about inactive products every day at 9:00 AM.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void notifyAdminForInactiveProducts() {
        log.info("Executing scheduled job: Notifying admin of inactive products.");

        List<Product> inactiveProducts = productRepository.findByIsActiveFalse();

        if (inactiveProducts.isEmpty()) {
            log.info("No inactive products found. Email notification not required.");
            return;
        }

        log.info("Found {} inactive products. Preparing to send notification.", inactiveProducts.size());

        String productIds = inactiveProducts.stream()
                .map(Product::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        String subject = "Daily Reminder: Products Pending Activation";
        String body = "Hi " + adminProps.getFirstname() + ",\n\n"
                + "This is an automated reminder that the following products are still awaiting activation:\n\n"
                + "Product IDs: " + productIds + "\n\n"
                + "Please review them in the admin panel.\n\n"
                + "Regards,\nSystem Notification";

        emailService.sendMail(adminProps.getEmail(), subject, body);
        log.info("Inactive product notification sent successfully to admin.");
    }
}