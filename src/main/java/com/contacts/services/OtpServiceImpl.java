package com.contacts.services;

import com.contacts.data.models.Otp;

import java.time.LocalDateTime;
import java.util.Random;

public class OtpServiceImpl implements OtpService{
    @Override
    public String generateOtp(String phoneNumber) {
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String otpCode = "";
        Random random = new Random();

        for (int count = 0; count < 6; count++) {
            otpCode += chars[random.nextInt(10)];

        }

        Otp otp = new Otp();
        otp.setPhoneNumber(phoneNumber);
        otp.setOtp()
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setUsedOtp(false);

        otps.save(otp);
        return otpCode;

    }

    @Override
    public boolean verifyOtp(String phoneNumber, String otpCode) {
        Otp otp = otps.findByPhoneNumber(phoneNumber);

        if (otp == null) {
            return false;
        }

        if (otp.isUsedOtp() ||
                LocalDateTime.now().isAfter(otp.getExpiresAt()) ||
                !otp.getOtpCode().equals(otpCode)) {
            return false;
        }

        otp.setUsedOtp(true);
        otps.save(otp);
        return true;
    }    }
}
