package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
