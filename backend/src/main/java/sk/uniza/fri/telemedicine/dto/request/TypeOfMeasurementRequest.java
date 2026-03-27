package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for creating new type of measurement.
 */
@Getter @AllArgsConstructor
public class TypeOfMeasurementRequest {

    @NotBlank(message = "Type name is mandatory")
    @Size(max=30)
    private String typeName;

    @NotBlank(message = "Units are mandatory")
    @Size(max=4)
    private String units;

    @Positive
    @NotNull(message = "Minimum value is mandatory")
    private Double minValue;

    @Positive
    @NotNull(message = "Maximum value is mandatory")
    private Double maxValue;

}
