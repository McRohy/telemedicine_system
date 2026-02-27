package sk.uniza.fri.telemedicine.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter @AllArgsConstructor
public class MeasurementPlanRequest {

    @NotNull(message = "Personal number is mandatory")
    private  String personalNumber;

    @NotNull(message = "Pan number is mandatory")
    private String panNumber;

    @NotNull(message = "Frequency is mandatory")
    private String  frequency;

    @NotNull(message = "Time of planned measurement is mandatory")
    @JsonFormat(pattern = "HH:mm") //Jackson map it to LocalTime
    private LocalTime timeOfPlannedMeasurements;

    @NotNull(message = "At least one type of measurement is required")
     private List<Integer> typeOfMeasurementIds;
}
