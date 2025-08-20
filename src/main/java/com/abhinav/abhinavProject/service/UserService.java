package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.ResetPasswordCO;

public interface UserService {
    void sendResetPasswordLink(String email);

    void resetUserPassword(ResetPasswordCO resetPasswordCO, String token);

}
