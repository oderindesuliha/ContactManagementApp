package com.contacts.data.repositories;

import com.contacts.data.models.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactRepositoryTest {

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
    public void saveTest() {
        Contact contact = new Contact();
        contact.setFirstName("Bola");
        contact.setLastName("Tinubu");
        contact.setEmail("b@gmail.com");
        contact.setPhoneNumber("09090909090");
        contact.setTags(new String[]{"friend"});
        Contact savedContact = contactRepository.save(contact);
        assertEquals(1, contactRepository.count());
        assertNotNull(savedContact.getId());
    }

//    @Test
//    public void testToSaveContact_findById() {
//        Contact contact = new Contact();
//        contact.setFirstName("Bola");
//        contact.setLastName("Tinubu");
//        Contact savedContact = contactRepository.save(contact);
//        Optional<Contact> foundContact = contactRepository.findById(savedContact.getId());
//        assertTrue(foundContact.isPresent());
//    }
//
//    @Test
//    public void testToSaveContact_findByPhoneNumber() {
//        Contact contact = new Contact();
//        contact.setFirstName("Bola");
//        contact.setLastName("Tinubu");
//        contact.setEmail("b@gmail.com");
//        contact.setPhoneNumber("09090909090");
//        contactRepository.save(contact);
//        assertEquals(1, contactRepository.count());
//
//        Optional<Contact> foundContact = contactRepository.findByPhoneNumber("09090909090");
//        assertTrue(foundContact.isPresent());
//        assertEquals("09090909090", foundContact.get().getPhoneNumber());
//    }
//
//    @Test
//    public void testToSaveContact_existsByPhoneNumber() {
//        Contact contact = new Contact();
//        contact.setFirstName("Yewande");
//        contact.setLastName("Tinubu");
//        contact.setPhoneNumber("08081828183");
//        contact.setEmail("Yewande@yahoo.com");
//        contactRepository.save(contact);
//        assertEquals(1, contactRepository.count());
//
//        boolean exists = contactRepository.existsByPhoneNumber("08081828183");
//        assertTrue(exists);
//        boolean notExists = contactRepository.existsByPhoneNumber("9999999999");
//        assertFalse(notExists);
//    }
//
//    @Test
//    public void testToSaveContact_findByTagsContaining() {
//        Contact contact1 = new Contact();
//        contact1.setFirstName("Bola");
//        contact1.setLastName("Tinubu");
//        contact1.setPhoneNumber("09090909090");
//        contact1.setTags(new String[]{"friend", "work"});
//        contactRepository.save(contact1);
//
//        Contact contact2 = new Contact();
//        contact2.setFirstName("Yewande");
//        contact2.setLastName("Tinubu");
//        contact2.setPhoneNumber("08081828183");
//        contact2.setTags(new String[]{"friend"});
//        contactRepository.save(contact2);
//
//        List<Contact> friendContacts = contactRepository.findByTagsContaining("friend");
//        assertEquals(2, friendContacts.size());
//
//        List<Contact> workContacts = contactRepository.findByTagsContaining("work");
//        assertEquals(1, workContacts.size());
//    }
//
//    @Test
//    public void testToSaveContact_linkToUser() {
//        User user = new User();
//        user.setFirstName("Bola");
//        user.setLastName("Tinubu");
//        user.setEmail("b@gmail.com");
//        userRepository.save(user);
//
//        Contact contact = new Contact();
//        contact.setFirstName("Yewande");
//        contact.setLastName("Tinubu");
//        contact.setPhoneNumber("08081828183");
//        contact.setTags(new String[]{"friend"});
//        Contact savedContact = contactRepository.save(contact);
//
//        user.setContactIds(List.of(savedContact.getId()));
//        userRepository.save(user);
//
//        User foundUser = userRepository.findByEmail("b@gmail.com");
//        assertEquals(1, foundUser.getContactIds().size());
//        assertEquals(savedContact.getId(), foundUser.getContactIds().get(0));
//    }
}

