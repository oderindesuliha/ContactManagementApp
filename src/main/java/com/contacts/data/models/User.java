package com.contacts.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean verified;
    private List<String> contactIds;


}
