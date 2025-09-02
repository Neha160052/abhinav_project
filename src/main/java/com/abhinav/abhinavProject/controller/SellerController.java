package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.AddressPatchDTO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.co.SellerProfileUpdateCO;
import com.abhinav.abhinavProject.co.SellerRegisterCO;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.CategoryService;
import com.abhinav.abhinavProject.service.SellerService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerDetailsDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SellerController {

    SellerService sellerService;
    MessageUtil messageUtil;
    private final CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerNewSeller(@RequestBody @Valid SellerRegisterCO sellerRegisterCO) {
        sellerService.registerSeller(sellerRegisterCO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(messageUtil.getMessage("seller.register.success")));
    }

    @GetMapping("/profile")
    public ResponseEntity<SellerDetailsDTO> getSellerProfile() {
        return ResponseEntity.ok(sellerService.getSellerDetails());
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse> updateSellerProfile(@RequestBody @Valid SellerProfileUpdateCO sellerProfileUpdateCO) {
        sellerService.updateSellerDetails(sellerProfileUpdateCO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(messageUtil.getMessage("profile.updated")));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<ApiResponse> updateSellerPassword(@RequestBody @Valid ResetPasswordCO resetPasswordCO) {
        sellerService.updateSellerPassword(resetPasswordCO);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("password.updated")));
    }

    @PatchMapping("/update-address")
    public ResponseEntity<ApiResponse> updateSellerAddress(@RequestParam long id,
                                                      @RequestBody @Valid AddressPatchDTO addressPatchDTO
    ) {
        sellerService.updateSellerAddress(id, addressPatchDTO);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("address.updated")));
    }

    @GetMapping("/category")
    public ResponseEntity<PageResponseVO<List<CategoryDetailsVO>>> getAllCategories(@PageableDefault(sort = "id") Pageable pageable) {
        PageResponseVO<List<CategoryDetailsVO>> responseVO = categoryService.getAllSellerCategories(pageable);
        return ResponseEntity.ok(responseVO);
    }
}
