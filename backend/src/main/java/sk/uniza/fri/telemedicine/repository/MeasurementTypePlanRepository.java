package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementTypePlan;

import java.util.List;

public interface MeasurementTypePlanRepository extends JpaRepository<MeasurementTypePlan, Integer> {

    @Query("SELECT mType FROM MeasurementTypePlan mType WHERE mType.measurementPlan.planId = :planId")
    List<MeasurementTypePlan> findAllByPlanId(Integer planId);

    @Query("SELECT COUNT(mtp) > 0 FROM MeasurementTypePlan mtp WHERE mtp.measurementPlan.patient.personalNumber = :personalNumber AND mtp.measurementPlan.validTo IS NULL AND mtp.typeOfMeasurement.typeId = :typeId")
    boolean existsByActivePlanAndTypeId(String personalNumber, Integer typeId);
}
