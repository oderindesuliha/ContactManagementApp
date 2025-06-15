package com.contacts.services;

import com.contacts.dtos.requests.VerifyOtpRequest;

public interface OtpService {
    String generateOtp(String userId, String phoneNumber);
    void verifyOtp(VerifyOtpRequest request);

}
