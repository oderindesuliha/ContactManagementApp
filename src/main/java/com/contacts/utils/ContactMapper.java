package com.contacts.utils;

import com.contacts.data.models.Contact;
import com.contacts.dtos.requests.ContactRequest;
import com.contacts.dtos.responses.ContactResponse;
import com.contacts.dtos.responses.MessageCard;

import java.time.LocalDate;

public class ContactMapper {
    public static Contact mapContactRequest(ContactRequest request) {
        Contact contact = new Contact();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setEmail(request.getEmail());
        contact.setFields(request.getFields());
        contact.setSpam(false);
        return contact;
    }

    public static ContactResponse mapContactResponse(Contact contact, String message) {
        ContactResponse response = new ContactResponse();
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setPhoneNumber(contact.getPhoneNumber());
        response.setEmail(contact.getEmail());
        response.setSpam(contact.isSpam());
        response.setFields(contact.getFields());
        response.setMessage(message);
        return response;
    }
}
