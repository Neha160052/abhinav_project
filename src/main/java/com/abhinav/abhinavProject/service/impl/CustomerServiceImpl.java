package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.*;
import com.abhinav.abhinavProject.entity.user.*;
import com.abhinav.abhinavProject.exception.*;
import com.abhinav.abhinavProject.repository.AddressRepository;
import com.abhinav.abhinavProject.repository.CustomerRepository;
import com.abhinav.abhinavProject.repository.RoleRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.security.UserPrinciple;
import com.abhinav.abhinavProject.service.CustomerService;
import com.abhinav.abhinavProject.service.ImageService;
import com.abhinav.abhinavProject.service.UserService;
import com.abhinav.abhinavProject.utils.MessageUtil;
import com.abhinav.abhinavProject.vo.CustomerDetailsDTO;
import com.abhinav.abhinavProject.vo.PageResponseVO;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerServiceImpl implements CustomerService {

    UserRepository userRepository;
    CustomerRepository customerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    EmailServiceImpl emailServiceImpl;
    UserService userService;
    AddressRepository addressRepository;
    ImageService imageService;
    MessageUtil messageUtil;

    public void registerCustomer(CustomerRegisterCO registerCO, MultipartFile file) {
        if (userRepository.existsByEmail(registerCO.getEmail())) {
            throw new ValidationException(messageUtil.getMessage("email.alreadyExists"));
        }

        if (!registerCO.getPassword().equals(registerCO.getConfirmPassword())) {
            throw new ValidationException(messageUtil.getMessage("password.mismatch"));
        }

        Role customerRole = roleRepository.findByAuthority("ROLE_CUSTOMER")
                .orElseThrow(() -> new RoleNotFoundException(messageUtil.getMessage("role.notFound")));

        User user = new User();
        Customer customer = new Customer();

        user.setEmail(registerCO.getEmail());
        user.setPassword(passwordEncoder.encode(registerCO.getPassword()));
        user.setFirstName(registerCO.getFirstName());
        if (registerCO.getMiddleName() != null)
            user.setMiddleName(registerCO.getMiddleName());
        user.setLastName(registerCO.getLastName());
        user.setRole(customerRole);

        customer.setUser(user);
        customer.setContact(Long.parseLong(registerCO.getPhoneNumber()));
        Customer newCustomer = generateNewActivationTokenAndSendEmail(customer);

        if (file != null && !file.isEmpty()) {
            try {
                imageService.save(file, newCustomer.getId());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store profile picture for user " + customer.getId(), e);
            }
        }

    }

    private Customer generateNewActivationTokenAndSendEmail(Customer customer) {
        ActivationToken activationToken = new ActivationToken();

        activationToken.setToken(UUID.randomUUID().toString());
        activationToken.setCustomer(customer);
        activationToken.setExpiration(LocalDateTime.now().plusHours(3));

        customer.setActivationToken(activationToken);

        Customer savedCustomer = customerRepository.save(customer);

        emailServiceImpl.sendActivationEmail(savedCustomer.getUser().getFirstName(),
                savedCustomer.getUser().getEmail(),
                savedCustomer.getActivationToken().getToken());

        return savedCustomer;
    }

    public void activateCustomerAccount(String token) {
        Customer customer = customerRepository.findByActivationToken_Token(token);

        if (customer == null) {
            throw new InvalidTokenException("Invalid activation token provided");
        }

        if (customer.getUser().isActive()) {
            throw new AccountActiveException("Customer account is already activated");
        }

        if (customer.getActivationToken()
                .getExpiration()
                .isBefore(LocalDateTime.now())
        ) {
            generateNewActivationTokenAndSendEmail(customer);
            throw new TokenExpiredException("Activation code expired. New activation link has been emailed.");
        }

        User customerUser = customer.getUser();
        customerUser.setActive(true);
        customer.setUser(customerUser);
        customer.setActivationToken(null);

        customerRepository.save(customer);
    }

    public void resendActivationCode(String email) {
        Customer existingCustomer = customerRepository.findByUser_Email(email)
                .orElseThrow(() -> new UserNotFoundException("Customer with email " + email + " does not exist"));

        if (existingCustomer.getUser().isActive()) {
            throw new AccountActiveException("Customer account is already activated");
        }
        generateNewActivationTokenAndSendEmail(existingCustomer);
    }

    public PageResponseVO<List<CustomerDetailsDTO>> getCustomers(String email, Pageable pageable) {
        Page<Customer> customers = customerRepository.findByUser_EmailContainsIgnoreCase(email, pageable);
        Page<CustomerDetailsDTO> detailsDTO = customers.map(CustomerDetailsDTO::new);
        detailsDTO = detailsDTO.map(this::setProfileImage);

        return new PageResponseVO<>(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                detailsDTO.hasNext(),
                detailsDTO.getContent()
        );
    }

    @Override
    public CustomerDetailsDTO getCustomerDetails() {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(()->new UserNotFoundException("Could not find customer details"));
        return setProfileImage(new CustomerDetailsDTO(customer));
    }

    @Override
    public Set<Address> getCustomerAddresses() {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not Found"));
        return user.getAddress();
    }

    @Override
    public void updateCustomerDetails(CustomerProfileUpdateCO customerProfileUpdateCO) {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUser_Email(principal.getUsername())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        User customerUser = customer.getUser();

        if(nonNull(customerProfileUpdateCO.getFirstName()))
            customerUser.setFirstName(customerProfileUpdateCO.getFirstName());
        if(nonNull(customerProfileUpdateCO.getMiddleName()))
            customerUser.setMiddleName(customerUser.getMiddleName());
        if(nonNull(customerProfileUpdateCO.getLastName()))
            customerUser.setLastName(customerProfileUpdateCO.getLastName());
        if(nonNull(customerProfileUpdateCO.getContact()))
            customer.setContact(Long.parseLong(customerProfileUpdateCO.getContact()));

        customer.setUser(customerUser);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomerPassword(ResetPasswordCO resetPasswordCO) {
        userService.updateUserPassword(resetPasswordCO);
    }

    @Override
    public void updateCustomerAddress(long id, AddressPatchDTO addressPatchDTO) {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateUserAddress(id, userPrinciple.getUsername(), addressPatchDTO);
    }

    @Override
    public void addCustomerAddress(AddressDTO addressDTO) {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = Address
                .builder()
                .user(user)
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .country(addressDTO.getCountry())
                .addressLine(addressDTO.getAddressLine())
                .zipCode(Integer.parseInt(addressDTO.getZipCode()))
                .label(addressDTO.getLabel())
                .build();

        user.getAddress().add(address);
        userRepository.save(user);
    }

    @Override
    public void deleteCustomerAddress(long addressId) {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("No User found"));

        if(!addressRepository.existsByIdAndUser_Id(addressId, user.getId())) {
            throw new AddressNotFoundException("Invalid Address id provided.");
        }

        addressRepository.deleteById(addressId);
    }

    private CustomerDetailsDTO setProfileImage(CustomerDetailsDTO dto) {
        try {
            if (imageService.fileExists(dto.getId()).isPresent()) {
                String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/static/")
                        .path(String.valueOf(dto.getId()))
                        .path("/profile-image")
                        .toUriString();
                dto.setProfileImage(uri);
            } else {
                dto.setProfileImage("No Image Uploaded.");
            }
            return dto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
