package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class TypeOfMeasurementRequest {

    @NotBlank(message = "Type name is mandatory")
    private String typeName;

    @NotBlank(message = "Units are mandatory")
    private String units;

    @NotNull(message = "Minimum value is mandatory")
    private Integer minValue;

    @NotNull(message = "Maximum value is mandatory")
    private Integer maxValue;

}
