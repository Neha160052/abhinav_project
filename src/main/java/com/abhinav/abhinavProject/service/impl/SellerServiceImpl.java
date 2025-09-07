package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.*;
import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.entity.user.Role;
import com.abhinav.abhinavProject.entity.user.Seller;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.exception.PasswordMismatchException;
import com.abhinav.abhinavProject.exception.RoleNotFoundException;
import com.abhinav.abhinavProject.exception.UserNotFoundException;
import com.abhinav.abhinavProject.repository.CustomerRepository;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.SellerRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.security.UserPrinciple;
import com.abhinav.abhinavProject.service.SellerService;
import com.abhinav.abhinavProject.service.UserService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import com.abhinav.abhinavProject.vo.SellerDetailsDTO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SellerServiceImpl implements SellerService {

    UserRepository userRepository;
    SellerRepository sellerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserService userService;
    MessageUtil messageUtil;
    CustomerRepository customerRepository;
    EmailServiceImpl emailServiceImpl;

    public Seller registerSeller(SellerRegisterCO registerCO) {
        if(userRepository.existsByEmail(registerCO.getEmail())) {
            throw new ValidationException(messageUtil.getMessage("email.alreadyExists"));
        }

        if(!registerCO.getPassword().equals(registerCO.getConfirmPassword())) {
            throw new PasswordMismatchException(messageUtil.getMessage("password.mismatch"));
        }

        if (sellerRepository.existsByGst(registerCO.getGst())) {
            throw new ValidationException(messageUtil.getMessage("gst.alreadyExists", registerCO.getGst()));
        }

        long contact = Long.parseLong(registerCO.getCompanyContact());
        if (customerRepository.existsByContact(contact) || sellerRepository.existsByCompanyContact(contact)) {
            throw new ValidationException(messageUtil.getMessage("contact.alreadyExists"));
        }

        if (sellerRepository.existsByCompanyNameIgnoreCase(registerCO.getCompanyName())) {
            throw new ValidationException(messageUtil.getMessage("company.name.alreadyExists"));
        }

        Role sellerRole = roleRepository.findByAuthority("ROLE_SELLER")
                .orElseThrow(() -> new RoleNotFoundException(messageUtil.getMessage("role.notFound")));

        User user = new User();
        Seller newSeller = new Seller();
        AddressCO companyAddress = registerCO.getCompanyAddress();

        user.setFirstName(registerCO.getFirstName());
        user.setMiddleName(registerCO.getMiddleName());
        user.setLastName(registerCO.getLastName());
        user.setRole(sellerRole);
        user.setEmail(registerCO.getEmail());
        user.setPassword(passwordEncoder.encode(registerCO.getPassword()));

        Address userAddress = Address.builder()
                .city(companyAddress.getCity())
                .state(companyAddress.getState())
                .country(companyAddress.getCountry())
                .addressLine(companyAddress.getAddressLine())
                .zipCode(Integer.parseInt(companyAddress.getZipCode()))
                .label(companyAddress.getLabel())
                .user(user)
                .build();

        user.setAddress(Set.of(userAddress));

        newSeller.setUser(user);
        newSeller.setCompanyName(registerCO.getCompanyName());
        newSeller.setCompanyContact(contact);
        newSeller.setGst(registerCO.getGst());

        Seller savedSeller = sellerRepository.save(newSeller);
        emailServiceImpl.sendSellerRegisteredEmail(savedSeller);
        return savedSeller;
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
        Seller seller = sellerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return new SellerDetailsDTO(seller);
    }

    @Override
    public void updateSellerDetails(SellerProfileUpdateCO sellerProfileUpdateCO) {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Seller seller = sellerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not Found"));
        User sellerUser = seller.getUser();

        String gst = sellerProfileUpdateCO.getGst();
        if (nonNull(gst)) {
            if (sellerRepository.existsByGst(gst)) {
                throw new ValidationException(messageUtil.getMessage("gst.alreadyExists", gst));
            }
            seller.setGst(gst);
        }

        if (hasText(sellerProfileUpdateCO.getCompanyContact())) {
            long contact = Long.parseLong(sellerProfileUpdateCO.getCompanyContact());
            if (customerRepository.existsByContact(contact) || sellerRepository.existsByCompanyContact(contact)) {
                throw new ValidationException(messageUtil.getMessage("contact.alreadyExists"));
            }
            seller.setCompanyContact(contact);
        }

        String companyName = sellerProfileUpdateCO.getCompanyName();
        if (nonNull(companyName)) {
            if (sellerRepository.existsByCompanyNameIgnoreCase(companyName)) {
                throw new ValidationException(messageUtil.getMessage("company.name.alreadyExists"));
            }
            seller.setGst(companyName);
        }

        if(nonNull(sellerProfileUpdateCO.getFirstName()))
            sellerUser.setFirstName(sellerProfileUpdateCO.getFirstName());

        if(nonNull(sellerProfileUpdateCO.getMiddleName()))
            sellerUser.setMiddleName(sellerUser.getMiddleName());

        if(nonNull(sellerProfileUpdateCO.getLastName()))
            sellerUser.setLastName(sellerProfileUpdateCO.getLastName());

        seller.setUser(sellerUser);

        sellerRepository.save(seller);
    }

    @Override
    public void updateSellerPassword(ResetPasswordCO resetPasswordCO) {
        userService.updateUserPassword(resetPasswordCO);
    }

    @Override
    public void updateSellerAddress(long id, AddressPatchDTO addressPatchDTO) {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateUserAddress(id, userPrinciple.getUsername(), addressPatchDTO);
    }
}
