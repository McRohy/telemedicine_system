package sk.uniza.fri.telemedicine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.constrains.MeasurementStatus;

import java.time.LocalDateTime;

@Getter //for Json serialization
@AllArgsConstructor
public class MeasurementRecordResponse {
        private String typeName;
        private Double value;
        private String units;
        @JsonFormat(pattern = "MM-dd HH:mm:ss")
        private LocalDateTime timeOfMeasurement;
        private MeasurementStatus status;
}
