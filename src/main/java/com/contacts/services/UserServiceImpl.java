package com.contacts.services;

import com.contacts.data.models.User;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;
import com.contacts.exceptions.ContactException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private OtpServiceImpl otpService;

        @Override
        public UserRegisterResponse registerUser(UserRegisterRequest request) {
            if (userRepository.findByEmail(request.getEmail()) != null) {
                throw new ContactException("Email already exists");
            }
            if (userRepository.findByPhoneNumber(request.getPhoneNumber()) != null) {
                throw new ContactException("Phone number already exists");
            }
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber()); // Store phoneNumber
            user = userRepository.save(user);
            otpService.generateOtp(user.getId(), request.getPhoneNumber());
            return mapToUserRegisterResponse(user, "Registration successful! Please verify your phone number with the OTP sent.");
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        return null;
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {

    }
}
