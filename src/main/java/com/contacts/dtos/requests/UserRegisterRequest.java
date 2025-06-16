package com.contacts.dtos.requests;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
}
