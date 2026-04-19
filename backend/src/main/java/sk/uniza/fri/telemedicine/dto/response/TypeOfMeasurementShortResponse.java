package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO containing  measurement type without min and max values.
 */
@Getter @AllArgsConstructor
public class TypeOfMeasurementShortResponse {
    private Long id;
    private String typeName;
    private String units;
}
