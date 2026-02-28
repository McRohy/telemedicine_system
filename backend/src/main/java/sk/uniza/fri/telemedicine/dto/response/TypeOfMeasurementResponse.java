package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class TypeOfMeasurementResponse {
    private Integer id;
    private String typeName;
    private String units;
    private Integer minValue;
    private Integer maxValue;

}
