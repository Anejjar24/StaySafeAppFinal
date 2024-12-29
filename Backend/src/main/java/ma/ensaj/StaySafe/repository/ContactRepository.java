package ma.ensaj.StaySafe.repository;



import ma.ensaj.StaySafe.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}