package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class MeasurementRecordRequest {

    @NotNull(message = "Personal number is mandatory")
    private Integer personalNumber;

    @NotNull(message = "Type of measurement is mandatory")
    private Integer typeOfMeasurementId;

    @NotNull(message = "Value is mandatory")
    private Integer value;

    private String note;
}
