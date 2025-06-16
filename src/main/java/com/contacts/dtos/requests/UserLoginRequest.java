package com.contacts.dtos.requests;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String phoneNumber;
    private String password;
}
