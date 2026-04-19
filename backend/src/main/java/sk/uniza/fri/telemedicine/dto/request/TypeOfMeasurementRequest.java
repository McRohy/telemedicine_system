package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for creating new type of measurement.
 */
@Getter @AllArgsConstructor
public class TypeOfMeasurementRequest {

    @NotBlank(message = "{validation.typeName.mandatory}")
    @Size(max = 30, message = "{validation.typeName.size}")
    private String typeName;

    @NotBlank(message = "{validation.units.mandatory}")
    @Size(max = 4, message = "{validation.units.size}")
    private String units;

    @Positive(message = "{validation.positive}")
    @NotNull(message = "{validation.minValue.mandatory}")
    private Double minValue;

    @Positive(message = "{validation.positive}")
    @NotNull(message = "{validation.maxValue.mandatory}")
    private Double maxValue;

}
