package com.contacts.services;

import com.contacts.data.models.User;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;
import com.contacts.exceptions.UserException;
import com.contacts.utils.Password;
import com.contacts.utils.UserMapper;
import com.contacts.validations.UserValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        UserValidations.validateUser(request);
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserException("User with this phone number already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email already in use");
        }
        User user = UserMapper.mapRegisterRequest(request);
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        return UserMapper.mapRegisterResponse(savedUser, "Account created successfully...OTP has been sent for verification");
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user == null) {
            throw new UserException("Invalid phone number or password");
        }

        if (!Password.checkPassword(request.getPassword(), user.getPassword()))
            throw new UserException("Invalid phone number or password");

        UserLoginResponse response = new UserLoginResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());

        return response;
    }

}