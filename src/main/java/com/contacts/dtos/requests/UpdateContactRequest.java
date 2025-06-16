package com.contacts.dtos.requests;

import lombok.Data;

@Data
public class UpdateContactRequest {
    private String userId;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String contactId;

}
