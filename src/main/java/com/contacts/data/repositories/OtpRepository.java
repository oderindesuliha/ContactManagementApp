package com.contacts.data.repositories;

import com.contacts.data.models.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findByOtpAndUserId(String otp, String userId);
    boolean existsByOtpAndUserId(String otp, String userId);

}
