package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.Patient;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {

    @Query("SELECT p FROM Patient p WHERE p.doctor.panNumber = :panNumber")
    Page<Patient> findAllByPanNumber(String panNumber, Pageable pageable);

    @Query("SELECT pd.email FROM Patient p JOIN p.doctor d JOIN d.personalData pd WHERE p.personalNumber = :personalNumber")
    Optional<String> findCareProviderEmailByPatientPersonalNumber(String personalNumber);

    @Query("SELECT CONCAT(pd.firstName, ' ', pd.lastName) FROM Patient p JOIN p.personalData pd WHERE p.personalNumber = :personalNumber")
    Optional<String> findFullNameByPersonalNumber(String personalNumber);

    @Query("SELECT p.personalNumber FROM Patient p JOIN p.personalData pd WHERE pd.email = :email")
    Optional<String> findPersonalNumberByEmail(String email);

    @Query("SELECT p FROM Patient p JOIN p.personalData pd WHERE LOWER(pd.lastName) LIKE LOWER(CONCAT(:searchLastName, '%'))")
    Page<Patient> findByPersonalDataLastNameStartingWithIgnoreCase(String searchLastName, Pageable pageable);

    @Query("SELECT p FROM Patient p JOIN p.personalData pd WHERE p.doctor.panNumber = :panNumber AND LOWER(pd.lastName) LIKE LOWER(CONCAT(:searchLastName, '%'))")
    Page<Patient> findByPanNumberAndPersonalDataLastNameStartingWithIgnoreCase(String panNumber, String searchLastName, Pageable pageable);
}
