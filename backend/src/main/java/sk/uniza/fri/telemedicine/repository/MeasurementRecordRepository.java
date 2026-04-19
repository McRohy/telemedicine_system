package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entity.MeasurementRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for accessing measurement record data.
 */
public interface MeasurementRecordRepository extends JpaRepository<MeasurementRecord, Long> {

    @Query("SELECT m FROM MeasurementRecord m WHERE m.patient.personalNumber = :personalNumber AND m.typeOfMeasurement.typeId= :typeId AND m.timeOfMeasurement >= :from AND m.timeOfMeasurement < :to")
    List<MeasurementRecord> findAllByPatientAndTimeBetween(String personalNumber, Long typeId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT m FROM MeasurementRecord m WHERE m.patient.personalNumber = :personalNumber")
    Page<MeasurementRecord> findByPersonalNumber(String personalNumber, Pageable pageable);

    @Query("SELECT m FROM MeasurementRecord m WHERE m.patient.personalNumber = :personalNumber AND m.typeOfMeasurement.typeId = :typeId")
    Page<MeasurementRecord> findByPersonalNumberAndMeasurementTypeId(String personalNumber, Long typeId, Pageable pageable);
}
