package com.contacts.services;

import com.contacts.dtos.requests.ContactRequest;
import com.contacts.dtos.requests.DeleteContactRequest;
import com.contacts.dtos.requests.PhoneNumberRequest;
import com.contacts.dtos.requests.UpdateContactRequest;
import com.contacts.dtos.responses.ContactResponse;

import java.util.List;

public interface ContactService {
    ContactResponse addContact(ContactRequest contactRequest);
    ContactResponse updateContact( UpdateContactRequest updateContactRequest);
    ContactResponse findContactByPhoneNumber (PhoneNumberRequest phoneNumberRequest);
    List<ContactResponse> findContactsByFields(String userId, String field);
    String deleteContact(DeleteContactRequest deleteContactRequest);
}
