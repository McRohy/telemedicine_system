package sk.uniza.fri.telemedicine.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enumeration.Frequency;

import java.time.LocalTime;
import java.util.List;

/**
 * Request DTO for creating and updating a measurement plan.
 */
@Getter @AllArgsConstructor
public class MeasurementPlanRequest {

    @NotBlank(message = "{validation.personalNumber.mandatory}")
    @Pattern(regexp = "\\d{10}", message = "{validation.personalNumber.format}")
    private  String personalNumber;

    @NotNull(message = "{validation.frequency.mandatory}")
    private Frequency frequency;

    @NotEmpty(message = "{validation.times.mandatory}")
    @JsonFormat(pattern = "HH:mm") //Jackson map it to LocalTime
    private List<LocalTime> timesOfPlannedMeasurements;

    @NotEmpty(message = "{validation.types.mandatory}") //controls null and empty list
    private List<Long> typeOfMeasurementIds;
}
