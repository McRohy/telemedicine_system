package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementTime;

import java.util.List;

public interface MeasurementTimeRepository extends JpaRepository<MeasurementTime, Integer> {

    List<MeasurementTime> findAllByPlanPlanId(Integer planId);

    // active
    @Query("SELECT mt FROM MeasurementTime mt WHERE mt.plan.planId = :planId AND mt.validTo IS NULL")
    List<MeasurementTime> findAllActiveTimesByMeasurementPlanId(Integer planId);
}
