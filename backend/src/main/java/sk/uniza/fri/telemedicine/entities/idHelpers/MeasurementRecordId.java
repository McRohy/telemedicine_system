package sk.uniza.fri.telemedicine.entities.idHelpers;

import lombok.Data;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import java.time.LocalDateTime;

@Data
public class MeasurementRecordId {
    private LocalDateTime timeOfMeasurement;
    private TypeOfMeasurement typeOfMeasurement;
    private Patient patient;
}
