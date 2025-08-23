package com.abhinav.abhinavProject.service;

import com.abhinav.abhinavProject.co.LoginRequestCO;
import com.abhinav.abhinavProject.co.ResetPasswordCO;
import com.abhinav.abhinavProject.vo.AuthTokenResponseVO;

public interface UserService {
    void sendResetPasswordLink(String email);

    void resetUserPassword(ResetPasswordCO resetPasswordCO, String token);

    String[] loginUser(LoginRequestCO loginRequestCO);

    String[] refreshJwtTokens(String refreshToken);
}
