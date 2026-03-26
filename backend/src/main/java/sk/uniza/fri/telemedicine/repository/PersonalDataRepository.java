package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.PersonalData;

import java.util.Optional;

/**
 * Repository for accessing personal data.
 */
public interface PersonalDataRepository extends JpaRepository<PersonalData, String> {

    @Query("SELECT pd FROM PersonalData pd WHERE pd.setupToken = :setupToken")
    Optional<PersonalData> findBySetupToken(String setupToken);
}
