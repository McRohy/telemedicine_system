package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.MeasurementPlanTypes;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementPlanComponentId;

public interface MeasurementPlanTypesRepository extends JpaRepository<MeasurementPlanTypes, MeasurementPlanComponentId> {
}
