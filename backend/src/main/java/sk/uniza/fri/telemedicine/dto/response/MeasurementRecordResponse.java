package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.others.StatusOfMeasurementRecord;

import java.time.LocalDateTime;

@Getter //for Json serialization
@AllArgsConstructor
public class MeasurementRecordResponse {
        private String typeName;
        private Integer value;
        private String units;
        private LocalDateTime timeOfMeasurement;
        private StatusOfMeasurementRecord status;
}
