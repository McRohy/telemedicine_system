package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.MeasurementPlanComponent;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementPlanComponentId;

public interface MeasurementPlanComponentRepository extends JpaRepository<MeasurementPlanComponent, MeasurementPlanComponentId> {
}
