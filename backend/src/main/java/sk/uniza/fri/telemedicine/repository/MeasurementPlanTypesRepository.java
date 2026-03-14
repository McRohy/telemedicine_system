package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementPlanTypes;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementPlanComponentId;

import java.util.List;

public interface MeasurementPlanTypesRepository extends JpaRepository<MeasurementPlanTypes, MeasurementPlanComponentId> {

    @Query("SELECT mpt FROM MeasurementPlanTypes mpt WHERE mpt.measurementPlan.planId = :planId")
    List<MeasurementPlanTypes> findAllByPlanId(Integer planId);

    // active
    @Query("SELECT mpt FROM MeasurementPlanTypes mpt WHERE mpt.measurementPlan.planId = :planId AND mpt.validTo IS NULL")
    List<MeasurementPlanTypes> findAllActiveTypesByMeasurementPlanId(Integer planId);

}
