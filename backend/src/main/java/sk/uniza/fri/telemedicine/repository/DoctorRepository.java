package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query("SELECT CONCAT(d.personalData.firstName, ' ', d.personalData.lastName) FROM Doctor d WHERE d.PanNumber = :panNumber")
    Optional<String> findFullNameByPanNumber(Integer panNumber);
}
