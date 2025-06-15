package com.contacts.dtos.requests;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String phoneNumber;
    private String otp;
}
