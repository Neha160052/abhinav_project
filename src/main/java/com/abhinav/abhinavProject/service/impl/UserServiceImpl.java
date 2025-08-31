package com.abhinav.abhinavProject.service.impl;

import com.abhinav.abhinavProject.co.AddressPatchDTO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.entity.user.Address;
import com.abhinav.abhinavProject.entity.user.User;
import com.abhinav.abhinavProject.exception.AddressNotFoundException;
import com.abhinav.abhinavProject.exception.UserNotFoundException;
import com.abhinav.abhinavProject.repository.AddressRepository;
import com.abhinav.abhinavProject.repository.UserRepository;
import com.abhinav.abhinavProject.security.UserPrinciple;
import com.abhinav.abhinavProject.service.UserService;
import com.abhinav.abhinavProject.utils.AuthUtils;
import com.abhinav.abhinavProject.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    EmailServiceImpl emailServiceImpl;
    AddressRepository addressRepository;
    AuthUtils authUtils;
    MessageUtil messageUtil;

    @Override
    public String activateUserAccount(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Invalid User id provided")
        );

        if (user.isActive()) {
            return messageUtil.getMessage("account.active");
        }

        user.setActive(true);
        userRepository.save(user);
        emailServiceImpl.sendAdminActivationMail(user);

        return messageUtil.getMessage("account.activated.success");
    }

    @Override
    public String deactivateUserAccount(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Invalid User id provided")
        );

        if (!user.isActive()) {
            return messageUtil.getMessage("account.isInactive");
        }

        user.setActive(false);
        userRepository.save(user);
        emailServiceImpl.sendAdminDeactivationMail(user);

        return messageUtil.getMessage("account.deactivated.success");
    }

    @Override
    public void updateUserPassword(ResetPasswordCO resetPasswordCO) {
        UserPrinciple principal = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        authUtils.resetUserPassword(user, resetPasswordCO.getPassword());
        emailServiceImpl.sendPasswordUpdateMail(user);
    }

    @Override
    public void updateUserAddress(long id, String email, AddressPatchDTO addressPatchDTO) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (nonNull(addressPatchDTO.getCity()))
            address.setCity(addressPatchDTO.getCity());
        if (nonNull(addressPatchDTO.getState()))
            address.setState(addressPatchDTO.getState());
        if (nonNull(addressPatchDTO.getCountry()))
            address.setCountry(addressPatchDTO.getCountry());
        if (nonNull(addressPatchDTO.getAddressLine()))
            address.setAddressLine(addressPatchDTO.getAddressLine());
        if (nonNull(addressPatchDTO.getZipCode()))
            address.setZipCode(Integer.parseInt(addressPatchDTO.getZipCode()));
        if (nonNull(addressPatchDTO.getLabel()))
            address.setLabel(addressPatchDTO.getLabel());

        addressRepository.save(address);
    }
}
