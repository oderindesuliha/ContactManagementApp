package com.contacts.services;

import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;


public interface UserService {
    UserRegisterResponse registerUser(UserRegisterRequest request);
    UserLoginResponse login(UserLoginRequest request);
    void verifyOtp(VerifyOtpRequest request);

}
