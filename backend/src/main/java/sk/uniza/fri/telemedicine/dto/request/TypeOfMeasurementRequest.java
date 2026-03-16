package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class TypeOfMeasurementRequest {

    @NotBlank(message = "Type name is mandatory")
    @Size(max=30)
    private String typeName;

    @NotBlank(message = "Units are mandatory")
    @Size(max=4)
    private String units;

    @NotNull(message = "Minimum value is mandatory")
    @Positive
    private Double minValue;

    @NotNull(message = "Maximum value is mandatory")
    @Positive
    private Double maxValue;

}
