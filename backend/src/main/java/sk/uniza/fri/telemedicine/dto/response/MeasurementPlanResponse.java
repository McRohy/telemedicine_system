package sk.uniza.fri.telemedicine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.Frequency;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MeasurementPlanResponse {
    private Integer id;
    private String personalNumber;
    private String panNumber;
    private Frequency frequency;

    private List<MeasurementPlanTypesResponse> typesOfMeasurements;

    @JsonFormat(pattern = "HH:mm")
    private List<LocalTime> timesOfPlannedMeasurements;

    @JsonFormat(pattern = "HH:mm:ss MM-dd-yyy")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "HH:mm:ss MM-dd-yyy")
    private LocalDateTime lastUpdateAt;
}
