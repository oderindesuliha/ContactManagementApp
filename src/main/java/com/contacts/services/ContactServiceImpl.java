package com.contacts.services;

import com.contacts.data.models.Contact;
import com.contacts.data.models.User;
import com.contacts.data.repositories.ContactRepository;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.ContactRequest;
import com.contacts.dtos.responses.ContactResponse;
import com.contacts.exceptions.ContactException;
import com.contacts.utils.ContactMapper;
import com.contacts.validations.ContactValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ContactResponse addContact(String userId, ContactRequest contactRequest) {
        ContactValidations.validateContact(contactRequest);

        if (contactRepository.existsByPhoneNumber(contactRequest.getPhoneNumber())) {
            throw new ContactException("Phone number already exists");
        }

        Optional<User> selectedUser = userRepository.findById(userId);
        if (!selectedUser.isPresent()) {
            throw new ContactException("User not found");
        }
        User user = selectedUser.get();

        Contact contact = ContactMapper.mapContactRequest(contactRequest);
        Contact savedContact = contactRepository.save(contact);

        if (user.getContactIds() == null) {
            user.setContactIds(new ArrayList<>());
        }
        user.getContactIds().add(savedContact.getId());
        userRepository.save(user);

        return ContactMapper.mapContactResponse(savedContact, "Contact saved successfully");
    }

    @Override
    public ContactResponse updateContact(String userId, ContactRequest contactRequest) {
        ContactValidations.validateContact(contactRequest);

        Optional<User> selectedUser = userRepository.findById(userId);
        if (!selectedUser.isPresent()) {
            throw new ContactException("User not found");
        }
        User user = selectedUser.get();

        Optional<Contact> selectedContact = contactRepository.findByPhoneNumber(contactRequest.getPhoneNumber());
        if (!selectedContact.isPresent()) {
            throw new ContactException("Contact not found!");
        }
        Contact contact = selectedContact.get();

        if (!user.getContactIds().contains(contact.getId())) {
            throw new ContactException("You can't update/delete this contact, it's not in your contact list");
        }

        contact.setFirstName(contactRequest.getFirstName());
        contact.setLastName(contactRequest.getLastName());
        contact.setEmail(contactRequest.getEmail());
        contact.setFields(contactRequest.getFields());

        Contact updatedContact = contactRepository.save(contact);

        return ContactMapper.mapContactResponse(updatedContact, "Contact updated successfully");
    }

    @Override
    public ContactResponse findContactByPhoneNumber(String phoneNo) {
        Optional<Contact> selectedContact = contactRepository.findByPhoneNumber(phoneNo);
        if (!selectedContact.isPresent()) {
            throw new ContactException("Contact not found!");
        }
        Contact contact = selectedContact.get();

        return ContactMapper.mapContactResponse(contact, "Contact found!");
    }

    @Override
    public List<ContactResponse> findContactsByFields(String userId, String field) {
        if (field == null || field.trim().isEmpty()) {
            throw new ContactException("Field cannot be empty!");
        }

        Optional<User> selectedUser = userRepository.findById(userId);
        if (!selectedUser.isPresent()) {
            throw new ContactException("User not found");
        }
        User user = selectedUser.get();

        List<Contact> contacts = contactRepository.findByFieldsContaining(field);
        List<ContactResponse> responses = new ArrayList<>();
        for (Contact contact : contacts) {
            if (user.getContactIds().contains(contact.getId())) {
                responses.add(ContactMapper.mapContactResponse(contact, "Contact found!"));
            }
        }
        return responses;
    }

    @Override
    public void deleteContact(String userId, String phoneNumber) {
        Optional<User> selectedUser = userRepository.findById(userId);
        if (!selectedUser.isPresent()) {
            throw new ContactException("User not found");
        }
        User user = selectedUser.get();

        Optional<Contact> selectedContact = contactRepository.findByPhoneNumber(phoneNumber);
        if (!selectedContact.isPresent()) {
            throw new ContactException("Contact not found");
        }
        Contact contact = selectedContact.get();

        if (!user.getContactIds().contains(contact.getId())) {
            throw new ContactException("You can't update/delete this contact, it's not in your contact list");
        }

        user.getContactIds().remove(contact.getId());
        userRepository.save(user);
        contactRepository.deleteByPhoneNumber(phoneNumber);
    }
}