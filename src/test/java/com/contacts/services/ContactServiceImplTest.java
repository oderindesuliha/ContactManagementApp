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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactServiceImplTest {

    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testToAddContact_shouldCreateContactSuccessfully() {
        User user = new User();
        user.setFirstName("Bola");
        user.setLastName("Tinubu");
        user.setEmail("b@gmail.com");
        User savedUser = userRepository.save(user);

        ContactRequest request = new ContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Tinubu");
        request.setPhoneNumber("09045676909");
        request.setEmail("y@gmail.com");
        request.setUserId(savedUser.getUserId());
        request.setFields(new String[]{"friend"});

        ContactResponse response = contactService.addContact(request);
        assertEquals("Yewande", response.getFirstName());
        assertEquals("Contact saved successfully", response.getMessage());

        User currentUser = userRepository.findById(savedUser.getUserId()).orElse(null);
        assertNotNull(currentUser);
        assertEquals(1, currentUser.getContactIds().size());
    }

    @Test
    public void testToAddContact_shouldFailToCreateContactWithInvalidPhoneNumber() {
        User user = new User();
        user.setFirstName("Bola");
        user.setLastName("Tinubu");
        user.setEmail("b@gmail.com");
        User savedUser = userRepository.save(user);

        ContactRequest request = new ContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Tinubu");
        request.setPhoneNumber("123456789");
        request.setUserId(savedUser.getUserId());
        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.addContact( request));
        assertEquals("Phone number must start with either 0 or +234", exception.getMessage());
    }

    @Test
    public void testToAddContact_UpdateContact_shouldUpdateContactSuccessfully() {
        User user = new User();
        user.setFirstName("Bola");
        user.setEmail("b@gmail.com");
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("09090909091");
        contact.setEmail("y@Tinubu.com");
        contact.setFields(new String[]{"old name"});
        contact.setUserId(savedUser.getUserId());
        Contact savedContact = contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Thomas");
        request.setPhoneNumber("09090909091");
        request.setEmail("y@gmail.com");
        request.setContactId(savedContact.getId());
        request.setUserId(savedUser.getUserId());

        ContactResponse response = contactService.updateContact(request);
        assertEquals("Yewande", response.getFirstName());
        assertEquals("Contact updated successfully", response.getMessage());
    }

    @Test
    public void testToAddContact_shouldFailToUpdate_WhenTheUserIdIsIncorrect_returnMessage() {
        User user = new User();
        user.setFirstName("Bola");
        user.setEmail("b@gmail.com");
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("07020150909");
        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Thomas");
        request.setPhoneNumber("07020150909");
        request.setUserId("Incorrect user id");

        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.updateContact(request));
        assertEquals("User not found", exception.getMessage());
    }


    @Test
    public void testToAddContact_shouldFailToUpdate_WhenTheContactIdIsIncorrect_returnMessage() {
        User user = new User();
        user.setFirstName("Bola");
        user.setEmail("b@gmail.com");
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("07020150909");
        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Thomas");
        request.setPhoneNumber("07020150909");
        request.setUserId(savedUser.getUserId());
        request.setContactId("incorrect contact id");

        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.updateContact(request));
        assertEquals("Contact not found!", exception.getMessage());
    }

    @Test
    public void testToAddContact_FindContactByPhoneNumber_returnMessage() {
        User user = new User();
        user.setFirstName("Bola");
        user.setEmail("b@gmail.com");
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("09090909092");
        contact.setUserId(savedUser.getUserId());
        contactRepository.save(contact);

        PhoneNumberRequest numberRequest = new PhoneNumberRequest();
        numberRequest.setPhoneNo("09090909092");
        numberRequest.setUserId(savedUser.getUserId());
        ContactResponse response = contactService.findContactByPhoneNumber(numberRequest);
        assertEquals("Yewande", response.getFirstName());
        assertEquals("Contact found!", response.getMessage());
    }

    @Test
    public void testToAddTwoContacts_findAnotherContact_shouldFailToFindContactByPhoneNumber() {
        User user = new User();
        user.setFirstName("Bola");
        user.setEmail("b@gmail.com");
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        Contact contact1 = new Contact();
        contact1.setFirstName("Yewande");
        contact1.setLastName("Tinubu");
        contact1.setPhoneNumber("09090909093");
        contact1.setUserId(savedUser.getUserId());
        contactRepository.save(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Ahmed");
        contact2.setLastName("Bello");
        contact2.setPhoneNumber("08180808081");
        contact2.setUserId(savedUser.getUserId());
        contactRepository.save(contact2);

        PhoneNumberRequest numberRequest = new PhoneNumberRequest();
        numberRequest.setPhoneNo("999999999");
        numberRequest.setUserId(savedUser.getUserId());
        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.findContactByPhoneNumber(numberRequest));
        assertEquals("Contact not found!", exception.getMessage());
    }

    @Test
    public void testToAddContact_shouldFindContactsByFields() {
        User user = new User();
        user.setFirstName("Bola");
        user.setLastName("Tinubu");
        user.setEmail("b@gmail.com");
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        Contact contact1 = new Contact();
        contact1.setFirstName("Yewande");
        contact1.setLastName("Tinubu");
        contact1.setPhoneNumber("09090909094");
        contact1.setFields(new String[]{"work"});
        contact1.setUserId(savedUser.getUserId());
        Contact savedContact1 = contactRepository.save(contact1);
        user.getContactIds().add(savedContact1.getId());

        Contact contact2 = new Contact();
        contact2.setFirstName("Ahmed");
        contact2.setLastName("Bello");
        contact2.setPhoneNumber("09190808082");
        contact2.setFields(new String[]{"work"});
        contact2.setUserId(savedUser.getUserId());
        Contact savedContact2 = contactRepository.save(contact2);
        user.getContactIds().add(savedContact2.getId());
        userRepository.save(user);

        Contact contact3 = new Contact();
        contact3.setFirstName("Olaide");
        contact3.setLastName("Junior");
        contact3.setPhoneNumber("09090919194");
        contact3.setFields(new String[]{"friend"});
        contact3.setUserId(savedUser.getUserId());
        Contact savedContact3 = contactRepository.save(contact3);
        user.getContactIds().add(savedContact3.getId());

        List<ContactResponse> responses = contactService.findContactsByFields(savedUser.getUserId(), "work");
        assertEquals(2, responses.size());
        assertEquals("Yewande", responses.get(0).getFirstName());
        assertEquals("Ahmed", responses.get(1).getFirstName());
    }

    @Test
    public void testToAddContact_DeleteContact_shouldDeleteContactSuccessfully() {
        User user = new User();
        user.setFirstName("Bola");
        user.setLastName("Tinubu");
        user.setEmail("b@gmail.com");
        user.setContactIds(new ArrayList<>());
        User savedUser = userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("09090909096");
        contact.setUserId(savedUser.getUserId());
        contactRepository.save(contact);

        DeleteContactRequest deleteRequest = new DeleteContactRequest();
        deleteRequest.setPhoneNumber("09090909096");
        deleteRequest.setUserId(savedUser.getUserId());
        assertEquals(contactService.deleteContact(deleteRequest), "Contact deleted successfully");
    }
}