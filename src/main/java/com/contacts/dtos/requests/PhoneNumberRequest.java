package com.contacts.dtos.requests;

import lombok.Data;

@Data
public class PhoneNumberRequest {
    private String userId;
    private String phoneNo;
}
