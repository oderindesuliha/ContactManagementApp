package com.contacts.controllers;

import com.contacts.dtos.requests.ContactRequest;
import com.contacts.dtos.responses.ContactResponse;
import com.contacts.dtos.responses.MessageCard;
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
    public ResponseEntity<ContactResponse> addContact(@RequestHeader("userId") String userId, @RequestBody ContactRequest request) {
        try {
            ContactResponse response = contactService.addContact(userId, request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ContactResponse error = new ContactResponse();
            error.setMessage(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateContact")
    public ResponseEntity<ContactResponse> updateContact(@RequestHeader("userId") String userId, @RequestBody ContactRequest request) {
        try {
            ContactResponse response = contactService.updateContact(userId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ContactResponse error = new ContactResponse();
            error.setMessage(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<ContactResponse> findContactByPhoneNumber(@PathVariable String phoneNumber) {
        try {
            ContactResponse response = contactService.findContactByPhoneNumber(phoneNumber);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ContactResponse error = new ContactResponse();
            error.setMessage(e.getMessage());
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

    @DeleteMapping("/phone/{phoneNumber}")
    public ResponseEntity<MessageCard> deleteContact(@RequestHeader("userId") String userId, @PathVariable String phoneNumber) {
        try {
            contactService.deleteContact(userId, phoneNumber);
            MessageCard messageCard = new MessageCard();
            messageCard.setMessage("Contact deleted successfully");
            return new ResponseEntity<>(messageCard, HttpStatus.OK);
        } catch (Exception e) {
            MessageCard error = new MessageCard();
            error.setMessage(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}