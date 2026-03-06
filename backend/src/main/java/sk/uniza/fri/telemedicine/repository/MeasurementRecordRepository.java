package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entities.MeasurementRecord;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementRecordId;

import java.time.LocalDate;
import java.util.List;

public interface MeasurementRecordRepository extends JpaRepository<MeasurementRecord, MeasurementRecordId> {

    @Query("SELECT m FROM MeasurementRecord m WHERE m.patient.personalNumber = :personalNumber AND m.typeOfMeasurement.typeId= :typeId AND CAST(m.timeOfMeasurement AS LocalDate) BETWEEN :from AND :to")
    List<MeasurementRecord> findAllByPatientAndTimeBetween(String personalNumber, Integer typeId, LocalDate from, LocalDate to);
}
