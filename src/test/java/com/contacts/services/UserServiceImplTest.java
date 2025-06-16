package com.contacts.services;

import com.contacts.data.repositories.UserRepository;
import com.contacts.dtos.requests.UserLoginRequest;
import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.responses.UserLoginResponse;
import com.contacts.dtos.responses.UserRegisterResponse;
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
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Oluwaseun");
        request.setLastName("Afolabi");
        request.setEmail("oluwaseun.afolabi@gmail.com");
        request.setPhoneNumber("09076543210");
        request.setPassword("<PASSWORD>");
        UserRegisterResponse response = userService.registerUser(request);
        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals("Account created successfully...OTP has been sent for verification", response.getMessage());
    }

    @Test
    void testRegisterUser_RegisterAnotherUserWithSameEmail_ThrowsException() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Oluwaseun");
        request.setLastName("Afolabi");
        request.setEmail("oluwaseun.afolabi@gmail.com");
        request.setPhoneNumber("09076543210");
        userService.registerUser(request);
        UserRegisterRequest duplicateRequest = new UserRegisterRequest();
        duplicateRequest.setFirstName("Tobi");
        duplicateRequest.setLastName("Ogunleye");
        duplicateRequest.setEmail("oluwaseun.afolabi@gmail.com");
        duplicateRequest.setPhoneNumber("08123456789");
        assertThrows(UserException.class, () -> userService.registerUser(duplicateRequest));
    }

    @Test
    void testRegisterUser_InvalidPhoneNumber_ThrowsException() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Oluwaseun");
        request.setLastName("Afolabi");
        request.setEmail("oluwaseun.afolabi@gmail.com");
        request.setPhoneNumber("123456");
        assertThrows(UserException.class, () -> userService.registerUser(request));
    }

    @Test
    void testUserLogin_LoginSuccessful() {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setFirstName("Chukwudi");
        registerRequest.setLastName("Okonkwo");
        registerRequest.setEmail("chukwudi.okonkwo@yahoo.com");
        registerRequest.setPhoneNumber("08198765432");
        registerRequest.setPassword("1234");
        userService.registerUser(registerRequest);

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setPhoneNumber("08198765432");
        loginRequest.setPassword("1234");
        UserLoginResponse response = userService.login(loginRequest);
        assertNotNull(response);
        assertEquals("Chukwudi", response.getFirstName());
        assertEquals("Okonkwo", response.getLastName());
    }

    @Test
    void testLogin_UnregisteredPhoneNumber_ThrowsException() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setPhoneNumber("07012345678");
        loginRequest.setPassword("1234");
        assertThrows(UserException.class, () -> userService.login(loginRequest));
    }


}