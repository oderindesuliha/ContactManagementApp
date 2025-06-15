package com.contacts.validations;

import com.contacts.dtos.requests.ContactRequest;
import com.contacts.exceptions.ContactException;

public class ContactValidations {
    public static void validateContact(ContactRequest contactRequest) {
        if (contactRequest.getFirstName() == null || contactRequest.getFirstName().trim().isEmpty()) {
            throw new ContactException("First name is required");
        }
        if (contactRequest.getLastName() == null || contactRequest.getLastName().trim().isEmpty()) {
            throw new ContactException("Last name is required");
        }

        if (contactRequest.getPhoneNumber() == null || contactRequest.getPhoneNumber().trim().isEmpty()) {
            throw new ContactException("Phone number is required");
        }
        if (contactRequest.getPhoneNumber().startsWith("0")) {
            String phoneNumber = contactRequest.getPhoneNumber().replaceAll("\\s", "").trim();
            if (!phoneNumber.matches("^0(70|80|81|90|91)[0-9]{8}$")) {
                throw new ContactException("Please enter a valid Nigerian phone number");
            }
        } else if (contactRequest.getPhoneNumber().startsWith("+234")) {
            String checkPhoneNumber = contactRequest.getPhoneNumber().replaceAll("\\s", "").trim();

            if (!checkPhoneNumber.matches("^\\+234(70|80|81|90|91)[0-9]{8}$")) {
                throw new ContactException("Please enter a valid Nigerian phone number");
            }
        } else {
            throw new ContactException("Phone number must start with either 0 or +234");
        }
    }
}

