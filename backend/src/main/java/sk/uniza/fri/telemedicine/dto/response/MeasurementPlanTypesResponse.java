package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class MeasurementPlanTypesResponse {
    private Long id;
    private String typeName;
    private String units;
}
