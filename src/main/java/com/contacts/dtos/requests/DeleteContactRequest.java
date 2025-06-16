package com.contacts.dtos.requests;

import lombok.Data;

@Data
public class DeleteContactRequest {
    private String userId;
    private String phoneNumber;

}