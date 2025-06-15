package com.contacts.validations;

import com.contacts.dtos.requests.UserRegisterRequest;
import com.contacts.dtos.requests.VerifyOtpRequest;
import com.contacts.exceptions.UserException;

public class UserValidations {
    public static void validateUser(UserRegisterRequest request) {
        if (request == null) {
            throw new UserException("Registration request cannot be null");
        }
        validateNames(request.getFirstName(), request.getLastName());
        validateEmail(request.getEmail());
        validatePhoneNumber(request.getPhoneNumber());
    }

    public static void validateOtp(VerifyOtpRequest request) {
        if (request == null || request.getPhoneNumber() == null || request.getOtp() == null) {
            throw new UserException("Invalid verification");
        }
        validatePhoneNumber(request.getPhoneNumber());
    }

    private static void validateNames(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty() || !firstName.matches("^[A-Za-z\\s-']{2,50}$")) {
            throw new UserException("First name must contain letters, spaces, or apostrophes (2-50 characters)");
        }
        if (lastName == null || lastName.trim().isEmpty() || !lastName.matches("^[A-Za-z\\s-']{2,50}$")) {
            throw new UserException("Last name must contain letters, spaces, or apostrophes (2-50 characters)");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            throw new UserException("Enter a valid email address");
        }
    }

    public static void validatePhoneNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new UserException("Phone number is required");
        }
        if (number.startsWith("0")) {
            String phoneNumber = number.replaceAll("\\s", "").trim();
            if (!phoneNumber.matches("^0(70|80|81|90|91)[0-9]{8}$")) {
                throw new UserException("Please enter a valid Nigerian phone number");
            }
        } else if (number.startsWith("+234")) {
            String checkPhoneNumber = number.replaceAll("\\s", "").trim();
            if (!checkPhoneNumber.matches("^\\+234(70|80|81|90|91)[0-9]{8}$")) {
                throw new UserException("Please enter a valid Nigerian phone number");
            }
        } else {
            throw new UserException("Phone number must start with either 0 or +234");

        }
    }
}