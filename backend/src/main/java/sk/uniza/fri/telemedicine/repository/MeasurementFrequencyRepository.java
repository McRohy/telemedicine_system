package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.MeasurementFrequency;

public interface MeasurementFrequencyRepository extends JpaRepository<MeasurementFrequency, Integer> {
}
