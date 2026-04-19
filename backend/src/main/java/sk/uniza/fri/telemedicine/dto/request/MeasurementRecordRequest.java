package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for recording health measurement.
 */
@Getter @AllArgsConstructor
public class MeasurementRecordRequest {

    @NotBlank(message = "{validation.personalNumber.mandatory}")
    @Pattern(regexp = "\\d{10}", message = "{validation.personalNumber.format}")
    private String personalNumber;

    @NotNull(message = "{validation.typeOfMeasurement.mandatory}")
    private Long typeOfMeasurementId;

    @Positive(message = "{validation.positive}")
    @NotNull(message = "{validation.value.mandatory}")
    private Double value;

    @Size(max = 220, message = "{validation.note.size}")
    private String note;
}
