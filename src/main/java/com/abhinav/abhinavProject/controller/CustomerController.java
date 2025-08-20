package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.CustomerRegisterCO;
import com.abhinav.abhinavProject.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CustomerController {

    CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody @Valid CustomerRegisterCO customerRegisterCO) {
        customerService.registerCustomer(customerRegisterCO);
        return ResponseEntity.ok("User Registered");
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateCustomerAccount(@RequestParam String token) {
        customerService.activateCustomerAccount(token);
            return ResponseEntity.ok("Customer account had been activated!");
    }
}
