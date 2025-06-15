package com.contacts.services;

import com.contacts.data.models.Otp;
import com.contacts.data.models.User;
import com.contacts.data.repositories.OtpRepository;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.VerifyOtpResponse;
import com.contacts.exceptions.ContactException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OtpServiceImplTest {
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpRepository otpRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        otpRepository.deleteAll();
    }

    @Test
    void testGenerateOtp() {
        User user = new User();
        user.setFirstName("Temitope");
        user.setLastName("Adeyemi");
        user.setEmail("temitope.adeyemi@gmail.com");
        user.setPhoneNumber("09087654321");
        user = userRepository.save(user);
        String otpCode = otpService.generateOtp("09087654321");
        assertNotNull(otpCode);
        assertEquals(6, otpCode.length());
        assertTrue(otpRepository.existsByOtpAndUserId(otpCode, user.getUserId()));
        Otp otp = otpRepository.findByPhoneNumber("09087654321").orElseThrow();
        assertEquals(user.getUserId(), otp.getUserId());
        assertFalse(otp.isUsed());
        assertTrue(otp.getOtpExpiryTime().isAfter(LocalDateTime.now()));
    }

    @Test
    void testVerifyOtp_Success() {
        User user = new User();
        user.setFirstName("Chidinma");
        user.setLastName("Eze");
        user.setEmail("chidinma.eze@yahoo.com");
        user.setPhoneNumber("08198765432");
        user = userRepository.save(user);
        String otpCode = otpService.generateOtp("08198765432");
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhoneNumber("08198765432");
        request.setOtp(otpCode);
        VerifyOtpResponse response = otpService.verifyOtp(request);
        assertTrue(response.isValidOtp());
        assertEquals("OTP verified successfully", response.getMessage());
        Otp otp = otpRepository.findByOtpAndUserId(otpCode, user.getUserId()).orElseThrow();
        assertTrue(otp.isUsed());
    }

    @Test
    void testVerifyOtp_InvalidOtp() {
        User user = new User();
        user.setFirstName("Sani");
        user.setLastName("Abubakar");
        user.setEmail("sani.abubakar@hotmail.com");
        user.setPhoneNumber("07012345678");
        user = userRepository.save(user);
        otpService.generateOtp("07012345678");
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhoneNumber("07012345678");
        request.setOtp("123456");
        VerifyOtpResponse response = otpService.verifyOtp(request);
        assertFalse(response.isValidOtp());
        assertEquals("Invalid OTP or phone number", response.getMessage());
    }

    @Test
    void testVerifyOtp_AlreadyUsed() {
        User user = new User();
        user.setFirstName("Zainab");
        user.setLastName("Ibrahim");
        user.setEmail("zainab.ibrahim@outlook.com");
        user.setPhoneNumber("+2349012345678");
        user = userRepository.save(user);
        String otpCode = otpService.generateOtp("+2349012345678");
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhoneNumber("+2349012345678");
        request.setOtp(otpCode);
        otpService.verifyOtp(request);
        VerifyOtpResponse response = otpService.verifyOtp(request);
        assertFalse(response.isValidOtp());
        assertEquals("OTP already used", response.getMessage());
    }

    @Test
    void testVerifyOtp_Expired() {
        User user = new User();
        user.setFirstName("Emeka");
        user.setLastName("Nwachukwu");
        user.setEmail("emeka.nwachukwu@gmail.com");
        user.setPhoneNumber("09134567890");
        user = userRepository.save(user);
        String otpCode = otpService.generateOtp("09134567890");
        Otp otp = otpRepository.findByOtpAndUserId(otpCode, user.getUserId()).get();
        otp.setOtpExpiryTime(LocalDateTime.now().minusMinutes(1));
        otpRepository.save(otp);
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhoneNumber("09134567890");
        request.setOtp(otpCode);
        VerifyOtpResponse response = otpService.verifyOtp(request);
        assertFalse(response.isValidOtp());
        assertEquals("OTP has expired", response.getMessage());
    }
}