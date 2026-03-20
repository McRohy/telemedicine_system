package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementTimePlan;
import sk.uniza.fri.telemedicine.entities.MeasurementTypePlan;

import java.util.List;

public interface MeasurementTimePlanRepository extends JpaRepository<MeasurementTimePlan, Long> {

    @Query("SELECT mTime FROM MeasurementTimePlan mTime WHERE mTime.measurementPlan.planId = :planId")
    List<MeasurementTimePlan> findAllByPlanId(Long planId);

}
