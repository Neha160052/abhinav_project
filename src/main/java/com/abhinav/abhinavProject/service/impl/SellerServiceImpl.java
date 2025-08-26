package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.AddressDTO;
import com.abhinav.abhinavProject.co.SellerRegisterCO;
import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.Seller;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.SellerRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.security.UserPrinciple;
import com.abhinav.abhinavProject.service.SellerService;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerDetailsDTO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SellerServiceImpl implements SellerService {

    UserRepository userRepository;
    SellerRepository sellerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public Seller registerSeller(SellerRegisterCO registerCO) {
        if(userRepository.existsByEmail(registerCO.getEmail())) {
            throw new ValidationException("Email Already Exists");
        }

        if(!registerCO.getPassword().equals(registerCO.getConfirmPassword())) {
            throw new ValidationException("Password mismatch.");
        }

        Role sellerRole = roleRepository.findByAuthority("ROLE_SELLER");

        User user = new User();
        user.setEmail(registerCO.getEmail());
        user.setPassword(passwordEncoder.encode(registerCO.getPassword()));
        user.setFirstName(registerCO.getFirstName());
        user.setLastName(registerCO.getLastName());
        user.setRole(sellerRole);

        AddressDTO companyAddress = registerCO.getCompanyAddress();
        Address userAddress = Address.builder()
                .city(companyAddress.getCity())
                .state(companyAddress.getState())
                .country(companyAddress.getCountry())
                .addressLine(companyAddress.getAddressLine())
                .zipCode(companyAddress.getZipCode())
                .label(companyAddress.getLabel())
                .build();

        user.setAddress(Set.of(userAddress));

        Seller newSeller = new Seller();
        newSeller.setUser(user);
        newSeller.setCompanyName(registerCO.getCompanyName());
        newSeller.setCompanyContact(Long.parseLong(registerCO.getCompanyContact()));
        newSeller.setGst(registerCO.getGst());

        return sellerRepository.save(newSeller);
    }

    @Override
    public PageResponseVO<List<SellerDetailsDTO>> getSellers(String email, Pageable pageable) {
        Page<Seller> sellers = sellerRepository.findByUser_EmailContainsIgnoreCase(email, pageable);
        Page<SellerDetailsDTO> detailsDTO = sellers.map(SellerDetailsDTO::new);

        return new PageResponseVO<>(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                detailsDTO.hasNext(),
                detailsDTO.getContent()
        );
    }

    @Override
    public SellerDetailsDTO getSellerDetails() {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Seller seller = sellerRepository.findByUser_Email(principal.getUsername()).get();
        return new SellerDetailsDTO(seller);
    }
}
