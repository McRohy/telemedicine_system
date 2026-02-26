package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.Frequency;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeasurementPlanResponse {
    private Integer id;
    private Integer personalNumber;
    private String timeOfPlannedMeasurements;
    private Frequency frequency;
    private List<String> typesOfMeasurements;
}
