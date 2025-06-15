package com.contacts.services;

import com.contacts.data.models.Otp;
import com.contacts.data.models.User;
import com.contacts.data.repositories.OtpRepository;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.VerifyOtpResponse;
import com.contacts.exceptions.ContactException;
import com.contacts.utils.OtpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    private static OtpRepository otpRepository;
    @Autowired
    private static UserRepository userRepository;

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user == null) {
            throw new ContactException("User not found");
        }
        Otp otp = otpRepository.findByOtpAndUserId(request.getOtp(), user.getUserId())
                .orElseThrow(() -> new ContactException("Invalid OTP or phone number"));
        if (otp.isUsed()) {
            return OtpMapper.mapOtpResponse(otp, false, "OTP already used");
        }
        if (LocalDateTime.now().isAfter(otp.getOtpExpiryTime())) {
            return OtpMapper.mapOtpResponse(otp, false, "OTP has expired");
        }
        otp.setUsed(true);
        otpRepository.save(otp);
        return OtpMapper.mapOtpResponse(otp, true, "OTP verified successfully");
    }

    public String generateOtp(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new ContactException("User not found");
        }
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String passCode = "";
        Random random = new Random();

        for (int count = 0; count < 6; count++) {
            passCode += chars[random.nextInt(10)];

        }

            String otpCode = generateOtp(phoneNumber);
            Otp otp = new Otp();
            otp.setUserId(user.getUserId());
            otp.setPhoneNumber(phoneNumber);
            otp.setOtp(otpCode);
            otp.setOtpGeneratedTime(LocalDateTime.now());
            otp.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
            otp.setUsed(false);
            otpRepository.save(otp);
            return otpCode;
        }

    }
