package com.contacts.controllers;

import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;
import com.contacts.exceptions.UserException;
import com.contacts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            UserRegisterResponse response = userService.registerUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception error) {
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        try {
            UserLoginResponse response = userService.login(request);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (UserException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception error) {
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

}