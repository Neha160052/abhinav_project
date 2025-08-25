package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.SellerRegisterCO;
import com.abhinav.abhinavProject.entity.user.Seller;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerDetailsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SellerService {
    Seller registerSeller(SellerRegisterCO sellerRegisterCO);
    PageResponseVO<List<SellerDetailsDTO>> getSellers(String email, Pageable pageable);
}
