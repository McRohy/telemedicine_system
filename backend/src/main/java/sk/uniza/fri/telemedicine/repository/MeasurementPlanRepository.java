package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementPlan;

import java.util.Optional;

public interface MeasurementPlanRepository extends JpaRepository<MeasurementPlan, Integer> {

    @Query("SELECT m FROM MeasurementPlan m WHERE m.patient.personalNumber = :personalNumber")
    Optional<MeasurementPlan>findByPersonalNumber(String personalNumber);
}
