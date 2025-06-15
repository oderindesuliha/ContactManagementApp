package com.contacts.services;

import com.contacts.dtos.requests.ContactRequest;
import com.contacts.dtos.responses.ContactResponse;

import java.util.List;

public interface ContactService {
    ContactResponse addContact(String userId, ContactRequest contactRequest);
    ContactResponse updateContact(String userId, ContactRequest contactRequest);
    ContactResponse findContactByPhoneNumber (String phoneNo);
    List<ContactResponse> findContactsByFields(String userId, String field);
    void deleteContact(String userId, String phoneNumber);
}
