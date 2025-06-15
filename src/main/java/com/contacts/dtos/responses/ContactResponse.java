package com.contacts.dtos.responses;

import lombok.Data;

@Data
public class ContactResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private boolean isSpam;
    private String[] fields;
    private String message;


}
