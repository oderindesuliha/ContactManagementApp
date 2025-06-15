package com.contacts.controllers;

import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.MessageCard;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;
import com.contacts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            UserRegisterResponse response = userService.registerUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            UserRegisterResponse error = new UserRegisterResponse();
            error.setMessage(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        try {
            UserLoginResponse response = userService.login(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            UserLoginResponse error = new UserLoginResponse();
            error.setMessage(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<MessageCard> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            userService.verifyOtp(request);
            MessageCard message = new MessageCard();
            message.setMessage("OTP verified successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            MessageCard error = new MessageCard();
            error.setMessage(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}