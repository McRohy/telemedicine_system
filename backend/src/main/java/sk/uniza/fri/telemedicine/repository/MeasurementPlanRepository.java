package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entity.MeasurementPlan;

import java.util.Optional;

/**
 * Repository for accessing measurement plan data.
 */
public interface MeasurementPlanRepository extends JpaRepository<MeasurementPlan, Long> {

    @Query("SELECT m FROM MeasurementPlan m WHERE m.patient.personalNumber = :personalNumber AND m.validTo IS NULL")
    Optional<MeasurementPlan> findActivePlanByPersonalNumber(String personalNumber);

    @Query("SELECT COUNT(m) > 0 FROM MeasurementPlan m WHERE m.patient.personalNumber = :personalNumber AND m.validTo IS NULL")
    boolean existsActivePlanByPersonalNumber(String personalNumber);
}
