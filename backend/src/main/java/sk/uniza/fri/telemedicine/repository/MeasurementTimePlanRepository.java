package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementTimePlan;

import java.util.List;

/**
 * Repository for accessing measurement time plan data.
 */
public interface MeasurementTimePlanRepository extends JpaRepository<MeasurementTimePlan, Long> {

    @Query("SELECT mTime FROM MeasurementTimePlan mTime WHERE mTime.measurementPlan.planId = :planId")
    List<MeasurementTimePlan> findAllByPlanId(Long planId);

}
