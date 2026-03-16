package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementRecord;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementRecordId;

import java.time.LocalDate;
import java.util.List;

public interface MeasurementRecordRepository extends JpaRepository<MeasurementRecord, MeasurementRecordId> {

    @Query("SELECT m FROM MeasurementRecord m WHERE m.patient.personalNumber = :personalNumber AND m.typeOfMeasurement.typeId= :typeId AND CAST(m.timeOfMeasurement AS LocalDate) BETWEEN :from AND :to")
    List<MeasurementRecord> findAllByPatientAndTimeBetween(String personalNumber, Integer typeId, LocalDate from, LocalDate to);

    @Query("SELECT m FROM MeasurementRecord m WHERE m.patient.personalNumber = :personalNumber  ORDER BY m.timeOfMeasurement DESC")
    Page<MeasurementRecord> findByPersonalNumberContainingIgnoreCaseOrderByTimeOfMeasurementDesc(String personalNumber, Pageable pageable);

    @Query("SELECT m FROM MeasurementRecord m WHERE m.patient.personalNumber = :personalNumber AND m.typeOfMeasurement.typeId = :typeId ORDER BY m.timeOfMeasurement DESC")
    Page<MeasurementRecord> findByPersonalNumberAndMeasurementTypeContainingIgnoreCaseOrderByTimeOfMeasurementDesc(String personalNumber, Integer typeId, Pageable pageable);
}
