package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.AddressPatchDTO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.co.SellerProfileUpdateCO;
import com.abhinav.abhinavProject.co.SellerRegisterCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
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
    public ResponseEntity<ApiResponse> registerNewSeller(@RequestBody @Valid SellerRegisterCO sellerRegisterCO) {
        sellerService.registerSeller(sellerRegisterCO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Seller Registered successfully"));
    }

    @GetMapping("/profile")
    public ResponseEntity<SellerDetailsDTO> getSellerProfile() {
        return ResponseEntity.ok(sellerService.getSellerDetails());
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse> updateSellerProfile(@RequestBody @Valid SellerProfileUpdateCO sellerProfileUpdateCO) {
        sellerService.updateSellerDetails(sellerProfileUpdateCO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Profile updated successfully"));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<ApiResponse> updateSellerPassword(@RequestBody @Valid ResetPasswordCO resetPasswordCO) {
        sellerService.updateSellerPassword(resetPasswordCO);
        return ResponseEntity.ok(new ApiResponse("Password updated successfully"));
    }

    @PatchMapping("/update-address")
    public ResponseEntity<ApiResponse> updateSellerAddress(@RequestParam long id,
                                                      @RequestBody @Valid AddressPatchDTO addressPatchDTO
    ) {
        sellerService.updateSellerAddress(id, addressPatchDTO);
        return ResponseEntity.ok(new ApiResponse("Address updated successfully"));
    }
}
