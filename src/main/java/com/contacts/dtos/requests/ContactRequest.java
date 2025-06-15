package com.contacts.dtos.requests;

import lombok.Data;

@Data
public class ContactRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String[] fields;
}
