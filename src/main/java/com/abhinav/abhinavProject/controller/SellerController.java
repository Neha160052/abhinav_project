package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.co.SellerProfileUpdateCO;
import com.abhinav.abhinavProject.co.SellerRegisterCO;
import com.abhinav.abhinavProject.service.SellerService;
import com.abhinav.abhinavProject.vo.SellerDetailsDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SellerController {

    SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerNewSeller(@RequestBody @Valid SellerRegisterCO sellerRegisterCO) {
        sellerService.registerSeller(sellerRegisterCO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<SellerDetailsDTO> getSellerProfile() {
        return ResponseEntity.ok(sellerService.getSellerDetails());
    }

    @PatchMapping("/profile")
    public ResponseEntity<String> updateSellerProfile(@RequestBody @Valid SellerProfileUpdateCO sellerProfileUpdateCO) {
        sellerService.updateSellerDetails(sellerProfileUpdateCO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Profile updated successfully");
    }


    @PatchMapping("/update-password")
    public ResponseEntity<String> updateSellerPassword(@RequestBody @Valid ResetPasswordCO resetPasswordCO) {
        sellerService.updateSellerPassword(resetPasswordCO);
        return ResponseEntity.ok("Password updated successfully");
    }
}
