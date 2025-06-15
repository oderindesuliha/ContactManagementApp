package com.contacts.services;

import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.VerifyOtpResponse;

public interface OtpService {
    String generateOtp(String phoneNumber);
    VerifyOtpResponse verifyOtp(VerifyOtpRequest request);

}
