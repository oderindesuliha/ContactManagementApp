package com.contacts.utils;

import com.contacts.data.models.Otp;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.VerifyOtpResponse;

public class OtpMapper {
    public static Otp mapOtpRequest(VerifyOtpRequest request, String userId) {
        Otp otp = new Otp();
        otp.setUserId(userId);
        otp.setPhoneNumber(request.getPhoneNumber());
        otp.setOtp(request.getOtp());
        return otp;
    }

    public static VerifyOtpResponse mapOtpResponse(Otp otp, boolean isValid, String message) {
        VerifyOtpResponse response = new VerifyOtpResponse();
        response.setOtpGeneratedTime(otp.getOtpGeneratedTime());
        response.setOtpExpiryTime(otp.getOtpExpiryTime());
        response.setValidOtp(isValid);
        response.setMessage(message);
        return response;
    }
}
