package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for recording health measurement.
 */
@Getter @AllArgsConstructor
public class MeasurementRecordRequest {

    @NotBlank(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Personal number must consist from 10 digits")
    private String personalNumber;

    @NotNull(message = "Type of measurement is mandatory")
    private Long typeOfMeasurementId;

    @Positive
    @NotNull(message = "Value is mandatory")
    private Double value;

    @Size(max = 220, message = "Note has maximum 220 characters")
    private String note;
}
