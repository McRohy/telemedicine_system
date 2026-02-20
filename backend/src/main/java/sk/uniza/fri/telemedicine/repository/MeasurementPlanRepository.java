package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.MeasurementPlan;

public interface MeasurementPlanRepository extends JpaRepository<MeasurementPlan, Integer> {
}
