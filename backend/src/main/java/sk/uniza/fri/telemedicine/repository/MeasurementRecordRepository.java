package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.MeasurementRecord;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementRecordId;

public interface MeasurementRecordRepository extends JpaRepository<MeasurementRecord, MeasurementRecordId> {

}
