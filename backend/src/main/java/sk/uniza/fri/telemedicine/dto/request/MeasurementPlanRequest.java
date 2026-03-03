package sk.uniza.fri.telemedicine.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter @AllArgsConstructor
public class MeasurementPlanRequest {

    @NotNull(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{11}", message = "Personal number must consist from 11 digits")
    private  String personalNumber;

    @NotNull(message = "Pan number is mandatory")
    @Pattern(regexp = "\\d{24}", message = "PAN number must consist from 24 digits")
    private String panNumber;

    @NotNull(message = "Frequency is mandatory")
    @Size(max=20)
    private String  frequency;

    @NotNull(message = "Time of planned measurement is mandatory")
    @JsonFormat(pattern = "HH:mm") //Jackson map it to LocalTime
    private LocalTime timeOfPlannedMeasurements;

    @NotNull(message = "At least one type of measurement is required")
     private List<Integer> typeOfMeasurementIds;
}
