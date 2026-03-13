package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.MeasurementTime;

import java.util.List;

public interface MeasurementTimeRepository extends JpaRepository<MeasurementTime, Integer> {

    List<MeasurementTime> findAllByPlanPlanId(Integer planId);

    void deleteAllByPlanPlanId(Integer planId);
}
