package com.abhinav.abhinavProject.service;


import com.abhinav.abhinavProject.co.AddressPatchDTO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;

public interface UserService {
    String activateUserAccount(long id);
    String deactivateUserAccount(long id);
    void updateUserPassword(ResetPasswordCO resetPasswordCO);
    void updateUserAddress(long id, String email, AddressPatchDTO addressPatchDTO);

}
