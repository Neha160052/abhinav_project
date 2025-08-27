package com.abhinav.abhinavProject.service;


import com.abhinav.abhinavProject.co.AddressPatchDTO;

public interface UserService {
    String activateUserAccount(long id);
    String deactivateUserAccount(long id);
    void updateUserAddress(long id, String email, AddressPatchDTO addressPatchDTO);

}
