package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.*;
import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.service.CustomerService;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
        return ResponseEntity.ok("Customer account has been activated!");
    }

    @GetMapping("/activate/resend")
    public ResponseEntity<String> resendActivationCode(@RequestBody @Valid EmailRequestCO emailRequestCO) {
        customerService.resendActivationCode(emailRequestCO.getEmail());
        return ResponseEntity.ok("New activation link has been sent to the registered email address!");
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerDetailsDTO> getCustomerDetails() {
        return ResponseEntity.ok(customerService.getCustomerDetails());
    }

    @GetMapping("/address")
    public ResponseEntity<Set<Address>> getCustomerAddresses() {
        return ResponseEntity.ok(customerService.getCustomerAddresses());
    }

    @PatchMapping("/profile")
    public ResponseEntity<String> updateCustomerProfile(@RequestBody @Valid CustomerProfileUpdateCO customerProfileUpdateCO) {
        customerService.updateCustomerDetails(customerProfileUpdateCO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Profile updated successfully");
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updateCustomerPassword(@RequestBody @Valid ResetPasswordCO resetPasswordCO) {
        customerService.updateCustomerPassword(resetPasswordCO);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PatchMapping("/update-address")
    public ResponseEntity<String> updateCustomerAddress(@RequestParam long id,
                                                        @RequestBody @Valid AddressPatchDTO addressPatchDTO
    ) {
        customerService.updateCustomerAddress(id, addressPatchDTO);
        return ResponseEntity.ok("Address updated successfully");
    }

    @PostMapping("/add-address")
    public ResponseEntity<String> addCustomerAddress(@RequestBody @Valid AddressDTO addressDTO) {
        customerService.addCustomerAddress(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Address added successfully.");
    }
}
