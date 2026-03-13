package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class MeasurementPlanTypesResponse {
    private Integer id;
    private String typeName;
}
