package com.contacts.controllers;

import com.contacts.dtos.requests.ContactRequest;
import com.contacts.dtos.requests.DeleteContactRequest;
import com.contacts.dtos.requests.PhoneNumberRequest;
import com.contacts.dtos.requests.UpdateContactRequest;
import com.contacts.dtos.responses.ContactResponse;
import com.contacts.exceptions.ContactException;
import com.contacts.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/saveContact")
    public ResponseEntity<?> addContact(@RequestBody ContactRequest request) throws Exception {
        try {
            ContactResponse response = contactService.addContact(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ContactException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateContact")
    public ResponseEntity<?> updateContact(@RequestBody UpdateContactRequest request) {
        try {
            ContactResponse response = contactService.updateContact(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ContactException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find contact")
    public ResponseEntity<?> findContactByPhoneNumber(@RequestBody PhoneNumberRequest phoneNumberRequest) {
        try {
            ContactResponse response = contactService.findContactByPhoneNumber(phoneNumberRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ContactException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception error) {
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/fields/{field}")
    public ResponseEntity<List<ContactResponse>> findContactsByFields(@RequestHeader("userId") String userId, @PathVariable String field) {
        try {
            List<ContactResponse> responses = contactService.findContactsByFields(userId, field);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            ContactResponse error = new ContactResponse();
            error.setMessage(e.getMessage());
            return new ResponseEntity<>(List.of(error), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-contact")
    public ResponseEntity<?> deleteContact(@RequestBody DeleteContactRequest deleteContactRequest) {
        try {
            System.out.println(deleteContactRequest.toString());
            contactService.deleteContact(deleteContactRequest);

            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        } catch (ContactException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception error) {
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}