package com.contacts.utils;


import com.contacts.data.models.User;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;

;

public class UserMapper {
    public static User mapRegisterRequest(UserRegisterRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setVerified(false);
        return user;
    }

    public static UserRegisterResponse mapRegisterResponse(User user, String message) {
        UserRegisterResponse response = new UserRegisterResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setContactIds(user.getContactIds());
        response.setMessage(message);
        return response;
    }

    public static UserLoginResponse mapLoginResponse(User user, String message) {
        UserLoginResponse response = new UserLoginResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setMessage(message);
        return response;
    }


}