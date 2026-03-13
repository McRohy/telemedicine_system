package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query("SELECT COUNT(p) > 0 FROM Patient p WHERE p.personalNumber = :personalNumber")
    boolean existsByPersonalNumber(String personalNumber);

    @Query("SELECT p FROM Patient p WHERE p.personalNumber = :personalNumber")
    Optional<Patient> findByPersonalNumber(String personalNumber);

    @Query("SELECT p FROM Patient p WHERE p.doctor.PanNumber = :panNumber")
    List <Patient> findAllByPanNumber(String panNumber);

    @Query("SELECT pd.email FROM Patient p JOIN p.doctor d JOIN d.personalData pd WHERE p.personalNumber = :personalNumber")
    Optional<String> findCareProviderEmailByPatientPersonalNumber(String personalNumber);

    @Query("SELECT CONCAT(pd.firstName, ' ', pd.lastName) FROM Patient p JOIN p.personalData pd WHERE p.personalNumber = :personalNumber")
    Optional<String> findFullNameByPernosalNumber(String personalNumber);

    @Query("SELECT p.personalNumber FROM Patient p JOIN p.personalData pd WHERE pd.email = :email")
    Optional<String> findPersonalNumberByEmail(String email);

}
