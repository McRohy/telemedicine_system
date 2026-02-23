package sk.uniza.fri.telemedicine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientResponse {
    private PersonalDataResponse personalData;
    private Integer doctorPanNumber;
}
