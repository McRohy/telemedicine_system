package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class MeasurementPlanTypesResponse {
    private Integer measurementPlanId;
    private Integer typeOfMeasurementId;
    private String typeName;
}
