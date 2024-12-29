package ma.ensaj.StaySafe.service;



import lombok.RequiredArgsConstructor;
import ma.ensaj.StaySafe.entity.Contact;
import ma.ensaj.StaySafe.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, Contact contact) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        existingContact.setName(contact.getName());
        existingContact.setNumber(contact.getNumber());
        return contactRepository.save(existingContact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }
}