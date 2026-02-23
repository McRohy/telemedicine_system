package sk.uniza.fri.telemedicine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter //for Json serialization
@AllArgsConstructor
public class MeasurementRecordResponse {
        private String typeName;
        private Integer value;
        private String units;
        private LocalDateTime timeOfMeasurement;
        private String status;
}
