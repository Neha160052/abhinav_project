package com.abhinav.abhinavProject.controller;

import com.abhinav.abhinavProject.exception.ApiResponse;
import com.abhinav.abhinavProject.service.CustomerService;
import com.abhinav.abhinavProject.service.SellerService;
import com.abhinav.abhinavProject.service.UserService;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerDetailsDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminController {

    CustomerService customerService;
    SellerService sellerService;
    UserService userService;

    @GetMapping("/customer")
    public ResponseEntity<PageResponseVO<List<CustomerDetailsDTO>>> getCustomerDetails(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(defaultValue = "", required = false) String email
    ) {
        return ResponseEntity.ok(customerService.getCustomers(email, pageable));
    }

    @GetMapping("/seller")
    public ResponseEntity<PageResponseVO<List<SellerDetailsDTO>>> getSellerDetails(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(defaultValue = "", required = false) String email
    ) {
        return ResponseEntity.ok(sellerService.getSellers(email, pageable));
    }

    @PatchMapping("/user/activate")
    public ResponseEntity<ApiResponse> activateUser(@RequestParam long id) {
        String response = userService.activateUserAccount(id);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PatchMapping("/user/deactivate")
    public ResponseEntity<ApiResponse> deactivateUser(@RequestParam long id) {
        String response = userService.deactivateUserAccount(id);
        return ResponseEntity.ok(new ApiResponse(response));
    }
}
