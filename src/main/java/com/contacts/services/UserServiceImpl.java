package com.contacts.services;

import com.contacts.data.models.User;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;
import com.contacts.dtos.responses.VerifyOtpResponse;
import com.contacts.exceptions.UserException;
import com.contacts.utils.UserMapper;
import com.contacts.validations.UserValidation;
import com.contacts.validations.UserValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;

    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        UserValidations.validateUser(request);
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserException("User with this phone number already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email already in use");
        }
        UserMapper.mapRegisterRequest(UserRegisterRequest request);
        User savedUser = userRepository.save(user);
        otpService.generateOtp(request.getPhoneNumber());
        UserRegisterResponse response = new UserRegisterResponse();
        response.setMessage("Account created successfully...OTP has been sent for verification");
        response.setUserId(savedUser.getUserId());
        return response;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        UserValidations.validatePhoneNumber(request.getPhoneNumber());
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user == null) {
            throw new UserException("User not found");
        }
        otpService.generateOtp(request.getPhoneNumber());
        UserLoginResponse response = new UserLoginResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setMessage("OTP sent for login verification.");
        return response;
    }

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        UserValidations.validateOtp(request);
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user == null) {
            throw new UserException("User not found");
        }
        VerifyOtpResponse response = otpService.verifyOtp(request);
        if (response.isValidOtp() && !user.isVerified()) {
            user.setVerified(true);
            userRepository.save(user);
        }
        return response;
    }

    @Override
    public void sendOtp(String phoneNumber) {
        UserValidations.validatePhoneNumber(phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UserException("User not found");
        }
        otpService.generateOtp(phoneNumber);
    }
}