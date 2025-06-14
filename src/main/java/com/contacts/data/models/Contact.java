package com.contacts.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "contacts")
public class Contact {

    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private boolean isSpam;
    private String[] tags;
}
