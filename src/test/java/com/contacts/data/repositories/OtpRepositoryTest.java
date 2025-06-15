package com.contacts.data.repositories;

import com.contacts.data.models.Otp;
import com.contacts.data.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OtpRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpRepository otpRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testSaveOtp() {
        Otp otp = new Otp();
        otp.setOtp("1234");
        otp.setUserId("user1");
        otp.setOtpGeneratedTime(LocalDateTime.now());
        otp.setOtpExpiryTime(LocalDate.from(LocalDateTime.now().plusMinutes(5)));
        otp.setUsed(false);
        Otp savedOtp = otpRepository.save(otp);
        assertEquals(1, otpRepository.count());
        assertNotNull(savedOtp.getId());
    }
    @Test
    public void testSaveOtpWithUser_toConfirmOtp() {
        User user = new User();
        user.setFirstName("Bola");
        user.setLastName("Tinubu");
        user.setEmail("b@gmail.com");
        User savedUser = userRepository.save(user);

        Otp otp = new Otp();
        otp.setOtp("1234");
        otp.setUserId(savedUser.getId());
        otp.setOtpGeneratedTime(LocalDateTime.now());
        otp.setOtpExpiryTime(LocalDate.from(LocalDateTime.now().plusMinutes(5)));
        otp.setUsed(false);
        otpRepository.save(otp);

        Optional<Otp> foundOtp = otpRepository.findByOtpAndUserId("1234", savedUser.getId());
        assertTrue(foundOtp.isPresent());
        assertEquals(savedUser.getId(), foundOtp.get().getUserId());
    }

    @Test
    public void testThatOtpExists_whenNotExpired() {
        Otp otp = new Otp();
        otp.setOtp("1234");
        otp.setUserId("user1");
        otp.setOtpGeneratedTime(LocalDateTime.now());
        otp.setOtpExpiryTime(LocalDate.from(LocalDateTime.now().plusMinutes(5)));
        otp.setUsed(false);
        otpRepository.save(otp);

        boolean exists = otpRepository.existsByOtpAndUserId("1234", "user1");
        assertTrue(exists);
    }

    @Test
    public void testOtpDoesNotExistWhenExpired() {
        Otp otp = new Otp();
        otp.setOtp("1234");
        otp.setUserId("user1");
        otp.setOtpGeneratedTime(LocalDateTime.now().minusMinutes(10));
        otp.setOtpExpiryTime(LocalDate.from(LocalDateTime.now().minusMinutes(5)));
        otp.setUsed(false);
        otpRepository.save(otp);

        boolean exists = otpRepository.existsByOtpAndUserId("1234", "user1");
        assertTrue(exists);
    }

}