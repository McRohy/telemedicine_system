package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entity.Doctor;

import java.util.Optional;

/**
 * Repository for accessing doctor data.
 */
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    @Query("SELECT d.panNumber FROM Doctor d JOIN d.personalData pd WHERE pd.email = :email")
    Optional<String> findPanNumberByEmail(String email);

    @Query("SELECT d FROM Doctor d JOIN d.personalData pd WHERE LOWER(pd.lastName) LIKE LOWER(CONCAT(:searchLastName, '%'))")
    Page<Doctor> findByPersonalDataLastNameStartingWithIgnoreCase(String searchLastName, Pageable pageable);
}
