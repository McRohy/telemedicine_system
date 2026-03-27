package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO containing measurement type details for measurement plans.
 */
@Getter @AllArgsConstructor
public class MeasurementPlanTypesResponse {
    private Long id;
    private String typeName;
    private String units;
}
