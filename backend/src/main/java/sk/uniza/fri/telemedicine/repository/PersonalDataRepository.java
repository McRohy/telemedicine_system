package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.PersonalData;

import java.util.Optional;

public interface PersonalDataRepository extends JpaRepository<PersonalData, String> {

    @Query("SELECT p FROM PersonalData p WHERE p.email = :email")
    Optional<PersonalData> findByEmail(String email);
}
