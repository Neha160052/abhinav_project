package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.co.*;
import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.CategoryService;
import com.abhinav.abhinavProject.service.CustomerService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.CategoryDetailsVO;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/customer")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CustomerController {

    CustomerService customerService;
    CategoryService categoryService;
    MessageUtil messageUtil;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse> registerCustomer(@RequestBody @Valid CustomerRegisterCO customerRegisterCO) {
        customerService.registerCustomer(customerRegisterCO);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("customer.register.success")));
    }

    @GetMapping("/activate")
    public ResponseEntity<ApiResponse> activateCustomerAccount(@RequestParam String token) {
        customerService.activateCustomerAccount(token);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("customer.account.activated")));
    }

    @GetMapping("/activate/resend")
    public ResponseEntity<ApiResponse> resendActivationCode(@RequestBody @Valid EmailRequestCO emailRequestCO) {
        customerService.resendActivationCode(emailRequestCO.getEmail());
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("customer.activation.resend")));
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
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("profile.updated")));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<ApiResponse> updateCustomerPassword(@RequestBody @Valid ResetPasswordCO resetPasswordCO) {
        customerService.updateCustomerPassword(resetPasswordCO);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("password.updated")));
    }

    @PostMapping("/profile-image")
    public ResponseEntity<ApiResponse> addCustomerProfileImage(MultipartFile image) {
        customerService.addCustomerProfileImage(image);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("profile.image.success")));
    }

    @PatchMapping("/update-address")
    public ResponseEntity<ApiResponse> updateCustomerAddress(@RequestParam long id,
                                                        @RequestBody @Valid AddressPatchDTO addressPatchDTO
    ) {
        customerService.updateCustomerAddress(id, addressPatchDTO);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("address.updated")));
    }

    @PostMapping("/add-address")
    public ResponseEntity<ApiResponse> addCustomerAddress(@RequestBody @Valid AddressCO addressCO) {
        customerService.addCustomerAddress(addressCO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(messageUtil.getMessage("customer.address.added")));
    }

    @DeleteMapping("/delete-address")
    public ResponseEntity<ApiResponse> deleteCustomerAddress(@RequestParam("id") long addressId) {
        customerService.deleteCustomerAddress(addressId);
        return ResponseEntity.ok(new ApiResponse(messageUtil.getMessage("customer.address.deleted")));
    }

    @GetMapping("/category")
    public ResponseEntity<PageResponseVO<List<CategoryDetailsVO>>> getAllCustomerCategories(@RequestParam(name = "id", required = false) Long id,
                                                                                            @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAllCustomerCategories(id, pageable));
    }

    @GetMapping("/category/filters/{id}")
    public ResponseEntity<CategoryDetailsVO> getCategoryDetailsForCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCustomerCategoryDetails(id));
    }
}
