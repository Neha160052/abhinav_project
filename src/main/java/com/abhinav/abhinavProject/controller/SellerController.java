package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.SellerRegisterCO;
import com.abhinav.abhinavProject.service.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SellerController {

    SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerNewSeller(@RequestBody SellerRegisterCO sellerRegisterCO) {
        sellerService.registerSeller(sellerRegisterCO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
