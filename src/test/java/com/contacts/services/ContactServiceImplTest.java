package com.contacts.services;

import com.contacts.data.models.Contact;
import com.contacts.data.models.User;
import com.contacts.data.repositories.ContactRepository;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.ContactRequest;
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

//    @BeforeEach
//    public void setUp() {
//        contactRepository.deleteAll();
//        userRepository.deleteAll();
//    }

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
        request.setFields(new String[]{"friend"});

        ContactResponse response = contactService.addContact(savedUser.getUserId(), request);
        assertEquals("Yewande", response.getFirstName());
        assertEquals("Contact saved successfully", response.getMessage());

        User currentUser = userRepository.findById(savedUser.getUserId()).orElse(null);
        assertNotNull(currentUser);
        assertEquals(1, currentUser.getContactIds().size());
    }

    @Test
    public void testToAddContact_shouldFailToCreateContactWithInvalidPhoneNumber() {
        ContactRequest request = new ContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Tinubu");
        request.setPhoneNumber("123456789");
        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.addContact("user1", request));
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
        contact.setEmail("y@old.com");
        contact.setFields(new String[]{"old"});
        Contact savedContact = contactRepository.save(contact);

        user.getContactIds().add(savedContact.getId());
        userRepository.save(user);

        ContactRequest request = new ContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Thomas");
        request.setPhoneNumber("09090909091");
        request.setEmail("y@gmail.com");
        request.setFields(new String[]{"friend"});

        ContactResponse response = contactService.updateContact(savedUser.getUserId(), request);
        assertEquals("Yewande", response.getFirstName());
        assertEquals("Contact updated successfully", response.getMessage());
    }

    @Test
    public void testToAddContact_shouldFailToUpdate_returnMessage() {
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

        ContactRequest request = new ContactRequest();
        request.setFirstName("Yewande");
        request.setLastName("Thomas");
        request.setPhoneNumber("07020150909");

        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.updateContact(savedUser.getUserId(), request));
        assertEquals("You can't update/delete this contact, it's not in your contact list", exception.getMessage());
    }

    @Test
    public void testToAddContact_FindContactByPhoneNumber_returnMessage() {
        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("09090909092");
        contactRepository.save(contact);

        ContactResponse response = contactService.findContactByPhoneNumber("09090909092");
        assertEquals("Yewande", response.getFirstName());
        assertEquals("Contact found!", response.getMessage());
    }

    @Test
    public void testToAddTwoContacts_findAnotherContact_shouldFailToFindContactByPhoneNumber() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Yewande");
        contact1.setLastName("Tinubu");
        contact1.setPhoneNumber("09090909093");
        contactRepository.save(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Ahmed");
        contact2.setLastName("Bello");
        contact2.setPhoneNumber("08180808081");
        contactRepository.save(contact2);

        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.findContactByPhoneNumber("9999999999"));
        assertEquals("Contact not found!", exception.getMessage());
    }

    @Test
    public void testToAddContactAndUser_shouldFindContactsByFields() {
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
        Contact savedContact1 = contactRepository.save(contact1);
        user.getContactIds().add(savedContact1.getId());

        Contact contact2 = new Contact();
        contact2.setFirstName("Ahmed");
        contact2.setLastName("Bello");
        contact2.setPhoneNumber("09190808082");
        contact2.setFields(new String[]{"work"});
        Contact savedContact2 = contactRepository.save(contact2);
        user.getContactIds().add(savedContact2.getId());
        userRepository.save(user);

        List<ContactResponse> responses = contactService.findContactsByFields(savedUser.getUserId(), "work");
        assertEquals(2, responses.size());
        assertEquals("Yewande", responses.get(0).getFirstName());
        assertEquals("Ahmed", responses.get(1).getFirstName());
    }

    @Test
    public void testToAddContactAndUser_DeleteContact_shouldDeleteContactSuccessfully() {
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
        contactRepository.save(contact);

        ContactException exception = assertThrows(ContactException.class, () ->
                contactService.deleteContact(savedUser.getUserId(), "09090909096"));
        assertEquals("You can't update/delete this contact, it's not in your contact list", exception.getMessage());
    }
}