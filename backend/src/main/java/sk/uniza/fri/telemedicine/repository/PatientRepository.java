package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
