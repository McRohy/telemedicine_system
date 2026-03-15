package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query("SELECT COUNT(d) > 0 FROM Doctor d WHERE d.PanNumber = :panNumber")
    boolean existsByPanNumber(String panNumber);

    @Query("SELECT d FROM Doctor d WHERE d.PanNumber = :panNumber")
    Optional<Doctor> findByPanNumber(String panNumber);

    @Query("SELECT CONCAT(d.personalData.firstName, ' ', d.personalData.lastName) FROM Doctor d WHERE d.PanNumber = :panNumber")
    Optional<String> findFullNameByPanNumber(String panNumber);

    @Query("SELECT d.PanNumber FROM Doctor d JOIN d.personalData pd WHERE pd.email = :email")
    Optional<String> findPanNumberByEmail(String email);

    @Query("SELECT d FROM Doctor d JOIN d.personalData pd WHERE LOWER(pd.lastName) LIKE LOWER(CONCAT(:searchLastName, '%'))")
    Page<Doctor> findByPersonalDataLastNameContainingIgnoreCase(String searchLastName, Pageable pageable);
}
