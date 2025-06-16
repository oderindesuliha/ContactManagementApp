package com.contacts.dtos.responses;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
