package com.contacts.data.repositories;

import com.contacts.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByFirstName(String firstName);
    User findByPhonenumber(String phoneNumber);
}
