package com.contacts.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "otps")
public class Otp {
    @Id
    private String id;
    private String phoneNumber;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    private LocalDate otpExpiryTime;
    private boolean otpVerified;
}
