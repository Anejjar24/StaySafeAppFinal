package ma.ensaj.StaySafe.controller;



import lombok.RequiredArgsConstructor;
import ma.ensaj.StaySafe.entity.Contact;
import ma.ensaj.StaySafe.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {
    @Autowired
    private final ContactService contactService;

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {
        return contactService.saveContact(contact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        return contactService.updateContact(id, contact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Contact getContact(@PathVariable Long id) {
        return contactService.getContactById(id);
    }
}
