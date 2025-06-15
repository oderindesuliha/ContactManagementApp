package com.contacts.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerifyOtpResponse {
    private LocalDateTime otpGeneratedTime;
    private LocalDateTime otpExpiryTime;
    private boolean isValidOtp;
    private String message;
}
