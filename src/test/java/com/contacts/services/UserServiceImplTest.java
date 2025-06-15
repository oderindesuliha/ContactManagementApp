package com.contacts.services;

import com.contacts.data.models.User;
import com.contacts.data.repositories.OtpRepository;
import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;
import com.contacts.dtos.responses.VerifyOtpResponse;
import com.contacts.exceptions.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
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
    void testRegisterUser() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Oluwaseun");
        request.setLastName("Afolabi");
        request.setEmail("oluwaseun.afolabi@gmail.com");
        request.setPhoneNumber("09076543210");
        UserRegisterResponse response = userService.register(request);
        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals("Account created successfully...OTP has been sent for verification", response.getMessage());
    }

    @Test
    void testRegisterUser_EmailExists_ThrowsException() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Oluwaseun");
        request.setLastName("Afolabi");
        request.setEmail("oluwaseun.afolabi@gmail.com");
        request.setPhoneNumber("09076543210");
        userService.register(request);
        UserRegisterRequest duplicateRequest = new UserRegisterRequest();
        duplicateRequest.setFirstName("Tobi");
        duplicateRequest.setLastName("Ogunleye");
        duplicateRequest.setEmail("oluwaseun.afolabi@gmail.com");
        duplicateRequest.setPhoneNumber("08123456789");
        assertThrows(UserException.class, () -> userService.register(duplicateRequest));
    }

    @Test
    void testRegisterUser_InvalidPhoneNumber_ThrowsException() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Oluwaseun");
        request.setLastName("Afolabi");
        request.setEmail("oluwaseun.afolabi@gmail.com");
        request.setPhoneNumber("123456");
        assertThrows(UserException.class, () -> userService.register(request));
    }

    @Test
    void testLogin_Success() {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setFirstName("Chukwudi");
        registerRequest.setLastName("Okonkwo");
        registerRequest.setEmail("chukwudi.okonkwo@yahoo.com");
        registerRequest.setPhoneNumber("08198765432");
        userService.register(registerRequest);
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setPhoneNumber("08198765432");
        UserLoginResponse response = userService.login(loginRequest);
        assertNotNull(response);
        assertEquals("Chukwudi", response.getFirstName());
        assertEquals("OTP sent for login verification.", response.getMessage());
    }

    @Test
    void testLogin_UnregisteredPhoneNumber_ThrowsException() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setPhoneNumber("07012345678");
        assertThrows(UserException.class, () -> userService.login(loginRequest));
    }

    @Test
    void testVerifyOtp_Success() {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setFirstName("Aminat");
        registerRequest.setLastName("Suleiman");
        registerRequest.setEmail("aminat.suleiman@hotmail.com");
        registerRequest.setPhoneNumber("+2349012345678");
        userService.register(registerRequest);
        User user = userRepository.findByPhoneNumber("+2349012345678");
        String otpCode = otpService.generateOtp("+2349012345678");
        VerifyOtpRequest verifyRequest = new VerifyOtpRequest();
        verifyRequest.setPhoneNumber("+2349012345678");
        verifyRequest.setOtp(otpCode);
        VerifyOtpResponse response = userService.verifyOtp(verifyRequest);
        assertTrue(response.isValidOtp());
        user = userRepository.findByPhoneNumber("+2349012345678");
        assertTrue(user.isVerified());
    }
}