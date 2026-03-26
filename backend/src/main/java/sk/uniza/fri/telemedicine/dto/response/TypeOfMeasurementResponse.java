package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO containing measurement type details.
 */
@Getter @AllArgsConstructor
public class TypeOfMeasurementResponse {
    private Long id;
    private String typeName;
    private String units;
    private Double minValue;
    private Double maxValue;

}
