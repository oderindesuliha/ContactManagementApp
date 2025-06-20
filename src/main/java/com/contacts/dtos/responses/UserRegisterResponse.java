package com.contacts.dtos.responses;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class UserRegisterResponse {

        private String userId;
        private String firstName;
        private String lastName;
        private String email;
        private List<String> contactIds;
        private String message;
    }

