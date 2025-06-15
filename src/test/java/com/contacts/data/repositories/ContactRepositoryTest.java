package com.contacts.data.repositories;

import com.contacts.data.models.Contact;
import com.contacts.data.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        contact.setFields(new String[]{"friend"});
        Contact savedContact = contactRepository.save(contact);
        assertEquals(1, contactRepository.count());
        assertNotNull(savedContact.getId());
    }

    @Test
    public void testToSaveContact_findById() {
        Contact contact = new Contact();
        contact.setFirstName("Bola");
        contact.setLastName("Tinubu");
        contact.setEmail("b@gmail.com");
        contact.setPhoneNumber("09090909090");
        contact.setFields(new String[]{"friend"});
        Contact savedContact = contactRepository.save(contact);
        Optional<Contact> foundContact = contactRepository.findById(savedContact.getId());
        assertTrue(foundContact.isPresent());
    }

    @Test
    public void testToSaveContact_findByPhoneNumber() {
        Contact contact = new Contact();
        contact.setFirstName("Bola");
        contact.setLastName("Tinubu");
        contact.setEmail("b@gmail.com");
        contact.setPhoneNumber("09090909090");
        contactRepository.save(contact);
        assertEquals(1, contactRepository.count());

        Optional<Contact> foundContact = contactRepository.findByPhoneNumber("09090909090");
        assertTrue(foundContact.isPresent());
        assertEquals("09090909090", foundContact.get().getPhoneNumber());
    }

    @Test
    public void testToSaveContact_findByFirstNameAndLastName_returnCount() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Bola");
        contact1.setLastName("Tinubu");
        contact1.setEmail("bt@gmail.com");
        contact1.setPhoneNumber("09090909090");
        contactRepository.save(contact1);
        assertEquals(1, contactRepository.count());

        Contact contact2 = new Contact();
        contact2.setFirstName("Yewande");
        contact2.setLastName("Tinubu");
        contact2.setPhoneNumber("08081828183");
        contact2.setEmail("Yewande@yahoo.com");
        contactRepository.save(contact2);
        assertEquals(2, contactRepository.count());

        Contact contact3 = new Contact();
        contact3.setFirstName("Tunde");
        contact3.setLastName("Taiwo");
        contact3.setPhoneNumber("09090563473");
        contactRepository.save(contact3);
        assertEquals(3, contactRepository.count());

        Optional<Contact> foundContact = contactRepository.findByFirstNameAndLastName("Bola", "Tinubu");
        assertEquals(3, contactRepository.count());
        assertTrue(foundContact.isPresent());
        assertEquals("Bola", foundContact.get().getFirstName());
        assertEquals("Tinubu", foundContact.get().getLastName());

    }

    @Test
    public void testToSaveContact_existsByPhoneNumber() {
        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("08081828183");
        contact.setEmail("Yewande@yahoo.com");

        contactRepository.save(contact);
        assertEquals(1, contactRepository.count());

        boolean phoneNumberExist = contactRepository.existsByPhoneNumber("08081828183");
        assertTrue(phoneNumberExist);
        boolean phoneNumberDoesNotExist = contactRepository.existsByPhoneNumber("9999999999");
        assertFalse(phoneNumberDoesNotExist);
    }

    @Test
    public void testToSaveContact_findByTagsContaining() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Bola");
        contact1.setLastName("Tinubu");
        contact1.setPhoneNumber("09090909090");
        contact1.setFields(new String[]{"friend", "work"});
        contactRepository.save(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Yewande");
        contact2.setLastName("Tinubu");
        contact2.setPhoneNumber("08081828183");
        contact2.setFields(new String[]{"friend"});
        contactRepository.save(contact2);

        List<Contact> friendContacts = contactRepository.findByFieldsContaining("friend");
        assertEquals(2, friendContacts.size());

        List<Contact> workContacts = contactRepository.findByFieldsContaining("work");
        assertEquals(1, workContacts.size());
    }

    @Test
    public void testToSaveContact_linkToUser() {
        User user = new User();
        user.setFirstName("Bola");
        user.setLastName("Tinubu");
        user.setEmail("b@gmail.com");
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setFirstName("Yewande");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("08081828183");
        contact.setFields(new String[]{"friend"});
        Contact saveContact = contactRepository.save(contact);
        userRepository.save(user);
        assertEquals(1, contactRepository.count());

        Contact contact1 = new Contact();
        contact1.setFirstName("Biodun");
        contact1.setLastName("Steve");
        contact1.setPhoneNumber("08081828183");
        contact1.setFields(new String[]{"friend"});
        Contact savedContact1 = contactRepository.save(contact1);;
        user.setContactIds(List.of(saveContact.getId(), savedContact1.getId()));
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("b@gmail.com");
        assertEquals(2, foundUser.getContactIds().size());
        assertEquals(saveContact.getId(), foundUser.getContactIds().get(0));
        assertEquals(savedContact1.getId(), foundUser.getContactIds().get(1));
    }

    @Test
    public void testDeleteByPhoneNumber_returnCount() {
        Contact contact = new Contact();
        contact.setFirstName("Bola");
        contact.setLastName("Tinubu");
        contact.setPhoneNumber("09090909090");
        contact.setEmail("b@gmail.com");
        contact.setFields(new String[]{"friend"});
        contactRepository.save(contact);

        assertTrue(contactRepository.existsByPhoneNumber("09090909090"));
        assertEquals(1, contactRepository.count());

        contactRepository.deleteByPhoneNumber("09090909090");

        assertFalse(contactRepository.existsByPhoneNumber("09090909090"));
        assertEquals(0, contactRepository.count());
    }

}

