package com.contacts.data.repositories;

import com.contacts.data.models.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {
    Optional<Contact> findByPhoneNumber(String PhoneNumber);
    void deleteByPhoneNumber(String phoneNumber);
    Contact save(Contact contact);
    boolean existsByPhoneNumber(String phoneNumber);
    List<Contact> findByFieldsContaining(String field);
    Optional<Contact> findByFirstNameAndLastName(String firstName, String lastName);
}
