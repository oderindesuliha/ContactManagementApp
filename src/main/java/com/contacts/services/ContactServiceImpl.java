package com.contacts.services;

import com.contacts.data.models.Contact;
import com.contacts.data.models.User;
import com.contacts.data.repositories.ContactRepository;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.ContactRequest;
import com.contacts.dtos.requests.DeleteContactRequest;
import com.contacts.dtos.requests.PhoneNumberRequest;
import com.contacts.dtos.requests.UpdateContactRequest;
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
    public ContactResponse addContact(ContactRequest contactRequest) {
       ContactValidations.validateContact(contactRequest);

        if (contactRepository.existsByPhoneNumber(contactRequest.getPhoneNumber())) {
            throw new ContactException("Phone number already exists");
        }

        Optional<User> selectedUser = userRepository.findById(contactRequest.getUserId());
        if (selectedUser.isEmpty()) {
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
    public ContactResponse updateContact(UpdateContactRequest updateContactRequest) {

        ContactValidations.validateUpdateContact(updateContactRequest);

        Optional<User> selectedUser = userRepository.findById(updateContactRequest.getUserId());
        if (selectedUser.isEmpty()) {
            throw new ContactException("User not found");
        }



        Optional<Contact> selectedContact = contactRepository.findById(updateContactRequest.getContactId());
        if (selectedContact.isEmpty()) {
            throw new ContactException("Contact not found!");
        }

        Contact contact = selectedContact.get();

        if(!contact.getUserId().equals(updateContactRequest.getUserId())) {
            throw new ContactException("You are not authorized to update this contact!");
        }


        contact.setFirstName(updateContactRequest.getFirstName());
        contact.setLastName(updateContactRequest.getLastName());
        contact.setEmail(updateContactRequest.getEmail());
        contact.setPhoneNumber(updateContactRequest.getPhoneNumber());

        Contact updatedContact = contactRepository.save(contact);

        return ContactMapper.mapContactResponse(updatedContact, "Contact updated successfully");
    }

    @Override
    public ContactResponse findContactByPhoneNumber(PhoneNumberRequest phoneNumberRequest) {

        Optional<User> selectedUser = userRepository.findById(phoneNumberRequest.getUserId());
        if (selectedUser.isEmpty()) {
            throw new ContactException("User not found");
        }

        Optional<Contact> selectedContact = contactRepository.findByPhoneNumber(phoneNumberRequest.getPhoneNo());
        if (selectedContact.isEmpty()) {
            throw new ContactException("Contact not found!");
        }
        if(!selectedContact.get().getUserId().equals(selectedUser.get().getUserId()))
            throw new ContactException("You are not authorized to view this contact!");

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
    public String deleteContact(DeleteContactRequest deleteContactRequest) {
        Optional<User> selectedUser = userRepository.findById(deleteContactRequest.getUserId());
        if (selectedUser.isEmpty()) {
            throw new ContactException("User not found");
        }
        User user = selectedUser.get();

        System.out.println(deleteContactRequest.getPhoneNumber());
        Optional<Contact> selectedContact = contactRepository.findByPhoneNumber(deleteContactRequest.getPhoneNumber());
        if (selectedContact.isEmpty()) {
            throw new ContactException("Contact not found");
        }
        Contact contact = selectedContact.get();

        if(!user.getUserId().equals(contact.getUserId()))
            throw new ContactException("You are not authorized to delete this contact!");

        contactRepository.delete(contact);
        return "Contact deleted successfully";
    }
}