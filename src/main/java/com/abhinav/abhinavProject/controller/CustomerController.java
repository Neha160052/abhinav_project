package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.*;
import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.CustomerService;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/customer")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CustomerController {

    CustomerService customerService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> registerCustomer(@RequestPart("profileData") @Valid CustomerRegisterCO customerRegisterCO,
                                                        @RequestPart(value = "profileImage", required = false) MultipartFile file) {
        customerService.registerCustomer(customerRegisterCO, file);
        return ResponseEntity.ok(new ApiResponse("Customer Registered successfully"));
    }

    @GetMapping("/activate")
    public ResponseEntity<ApiResponse> activateCustomerAccount(@RequestParam String token) {
        customerService.activateCustomerAccount(token);
        return ResponseEntity.ok(new ApiResponse("Customer account has been activated!"));
    }

    @GetMapping("/activate/resend")
    public ResponseEntity<ApiResponse> resendActivationCode(@RequestBody @Valid EmailRequestCO emailRequestCO) {
        customerService.resendActivationCode(emailRequestCO.getEmail());
        return ResponseEntity.ok(new ApiResponse("New activation link has been sent to the registered email address"));
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
    public ResponseEntity<ApiResponse> updateCustomerProfile(@RequestBody @Valid CustomerProfileUpdateCO customerProfileUpdateCO) {
        customerService.updateCustomerDetails(customerProfileUpdateCO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Profile updated successfully"));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<ApiResponse> updateCustomerPassword(@RequestBody @Valid ResetPasswordCO resetPasswordCO) {
        customerService.updateCustomerPassword(resetPasswordCO);
        return ResponseEntity.ok(new ApiResponse("Password updated successfully"));
    }

    @PatchMapping("/update-address")
    public ResponseEntity<ApiResponse> updateCustomerAddress(@RequestParam long id,
                                                        @RequestBody @Valid AddressPatchDTO addressPatchDTO
    ) {
        customerService.updateCustomerAddress(id, addressPatchDTO);
        return ResponseEntity.ok(new ApiResponse("Address updated successfully"));
    }

    @PostMapping("/add-address")
    public ResponseEntity<ApiResponse> addCustomerAddress(@RequestBody @Valid AddressDTO addressDTO) {
        customerService.addCustomerAddress(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Address added successfully."));
    }

    @DeleteMapping("/delete-address")
    public ResponseEntity<String> deleteCustomerAddress(@RequestParam("id") long addressId) {
        customerService.deleteCustomerAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
