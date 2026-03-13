package sk.uniza.fri.telemedicine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MeasurementPlanResponse {
    private Integer id;
    private String personalNumber;
    private String panNumber;
    private String frequency;

    private List<MeasurementPlanTypesResponse> typesOfMeasurements;

    @JsonFormat(pattern = "HH:mm")
    private List<LocalTime> timesOfPlannedMeasurements;

    @JsonFormat(pattern = "HH:mm:ss MM-dd-yyy")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "HH:mm:ss MM-dd-yyy")
    private LocalDateTime lastUpdateAt;

    @JsonProperty("typeOfMeasurementIds")
    public List<Integer> getTypeOfMeasurementIds() {
        return typesOfMeasurements.stream()
                .map(MeasurementPlanTypesResponse::getId)
                .toList();
    }
}
