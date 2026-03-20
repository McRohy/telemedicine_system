package sk.uniza.fri.telemedicine.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.Frequency;

import java.time.LocalTime;
import java.util.List;

@Getter @AllArgsConstructor
public class MeasurementPlanRequest {

    @NotNull(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Personal number must consist from 10 digits")
    private  String personalNumber;

    @NotNull(message = "Frequency is mandatory")
    private Frequency frequency;

    @NotEmpty(message = "Time is mandatory")
    @JsonFormat(pattern = "HH:mm") //Jackson map it to LocalTime
    private List<LocalTime> timesOfPlannedMeasurements;

    @NotEmpty(message = "Types are mandatory") //controls null and empty list
    private List<Long> typeOfMeasurementIds;
}
