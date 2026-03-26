package sk.uniza.fri.telemedicine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.MeasurementStatus;

import java.time.LocalDateTime;

/**
 * Response DTO containing a single measurement record details.
 */
@Getter @AllArgsConstructor
public class MeasurementRecordResponse {
        private Long id;
        private String typeName;
        private Double value;
        private String units;
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
        private LocalDateTime timeOfMeasurement;
        private MeasurementStatus status;
        private String note;
}
