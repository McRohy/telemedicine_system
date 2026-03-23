package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class MeasurementRecordRequest {

    @NotBlank(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Personal number must consist from 10 digits")
    private String personalNumber;

    @NotNull(message = "Type of measurement is mandatory")
    private Long typeOfMeasurementId;

    @NotNull(message = "Value is mandatory")
    private Double value;

    private String note;
}
