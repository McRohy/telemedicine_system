package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.PersonalData;

public interface PersonalDataRepository extends JpaRepository<PersonalData, String> {
}
